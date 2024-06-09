

kubectl apply -f iscv.yaml

<!-- NOT working on KF 1.9-rc.1 M2M

SERVICE_HOSTNAME=$(kubectl get inferenceservice mm-model -n default -o jsonpath='{.status.url}' | cut -d "/" -f 3)
echo $SERVICE_HOSTNAME


export KF_TOKEN="$(kubectl -n default create token default)"
curl -v \
  -H "Authorization: Bearer "$KF_TOKEN \
  -H "Host: ${SERVICE_HOSTNAME}" \
  -H "Content-Type: application/json" \
  -d @./post.json \
  http://localhost:8080/v2/models/mm-model/infer

-->

# Intra cluster

curl -v \
  -H "Content-Type: application/json" \
  -d @./post.json \
  http://mm-model.default.svc.cluster.local/v2/models/mm-model/infer

# manually wiring ports

kubectl port-forward -n default pod/mm-model-predictor-00001-deployment-7f778446-54p2v 8090:8080 --address=0.0.0.0
curl -v \
  -H "Content-Type: application/json" \
  -d @./post.json \
  http://localhost:8090/v2/models/mm-model/infer


## Based on:

- Apache-2.0 https://www.kaggle.com/code/gerardocappa/card-fraud-detection-with-randomforest
- 
