#!/bin/sh

echo "Usage $0 (ingester | moviesdb | peopledb)"

kubectl create -n movie-store -f tekton/pipelinerun/deploy-$1-service.yaml