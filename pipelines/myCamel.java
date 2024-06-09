///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS org.apache.camel:camel-bom:4.4.2@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-http
//DEPS org.apache.camel:camel-zipfile
//DEPS org.apache.camel:camel-file
//DEPS org.apache.camel:camel-main
//DEPS org.slf4j:slf4j-simple:2.0.13

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.main.Main;

@Command(name = "myCamel", mixinStandardHelpOptions = true, version = "myCamel 0.1",
        description = "myCamel made with jbang")
class myCamel implements Callable<Integer> {

    @Parameters(index = "0", description = "The directory where to store", defaultValue = "/data")
    private String target;

    public static void main(String... args) {
        int exitCode = new CommandLine(new myCamel()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                String githubUrl = "https://raw.githubusercontent.com/tarilabs/demo20240608-KFcomponents/main/pipelines/archive.zip";
                String outputDirectory = target;

                from("timer:start?repeatCount=1")
                    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .toD(githubUrl)
                    .convertBodyTo(byte[].class)
                    .to("file:temp?fileName=downloaded.zip")
                    .split(new ZipSplitter()).streaming()
                        .to("file:" + outputDirectory)
                    .end()
                    .log("File unzipped successfully to: " + outputDirectory)
                    .process(exchange -> {
                        var context = exchange.getContext();
                        new Thread(() -> { // do not block exchange thread
                            try {
                                Thread.sleep(1000); // Giving some grace finish logging
                                context.stop(); // stop camel context
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    });
            }
        });
        return main.run(new String[]{});
    }
}
