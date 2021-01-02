#!/bin/sh

# Setup the namespace
echo Creating kubernetes namespace - movie-store
kubectl create namespace movie-store

# Setup the ServiceAccount in which Tekton will run (in the movie-store namespace)
echo Creating the ServiceAccount in which Tekton will run
kubectl -n movie-store create serviceaccount deploy-pipeline-run-sa -o yaml --dry-run | kubectl apply -f -

echo Creating the rolebinding for the ServiceAccount
kubectl -n movie-store create rolebinding deploy-pipeline-run-sa-edit --clusterrole edit --serviceaccount movie-store:deploy-pipeline-run-sa -o yaml --dry-run | kubectl apply -f -

echo Creating the volume claims
kubectl apply -n movie-store  -f tekton/resources/deploy-ingester-pvc.yaml
kubectl apply -n movie-store  -f tekton/resources/deploy-moviesdb-pvc.yaml
kubectl apply -n movie-store  -f tekton/resources/deploy-peopledb-pvc.yaml

echo Registering the tekton pipelines
kubectl apply -n movie-store -f tekton/pipelines/deploy-ingester-pipeline.yaml
kubectl apply -n movie-store -f tekton/pipelines/deploy-moviesdb-pipeline.yaml
kubectl apply -n movie-store -f tekton/pipelines/deploy-peopledb-pipeline.yaml

