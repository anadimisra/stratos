#!/bin/bash

iaas="mock"
host_ip="localhost"
host_port=9443

script_path="$( cd -P "$( dirname "$SOURCE" )" && pwd )/`dirname $0`"
artifacts_path="${script_path}/../../artifacts"
iaas_artifacts_path="${script_path}/../../artifacts/${iaas}"
cartridges_path="${script_path}/../../../../cartridges/${iaas}"
cartridges_groups_path="${script_path}/../../../../cartridges-groups"

set -e

echo ${artifacts_path}/autoscale-policy.json
echo "Adding autoscale policy..."
curl -X POST -H "Content-Type: application/json" -d "@${artifacts_path}/autoscale-policy.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/autoscalingPolicies

echo "Adding tomcat cartridge..."
curl -X POST -H "Content-Type: application/json" -d "@${cartridges_path}/tomcat.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/cartridges

echo "Adding tomcat1 cartridge..."
curl -X POST -H "Content-Type: application/json" -d "@${cartridges_path}/tomcat1.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/cartridges

echo "Adding tomcat2 cartridge..."
curl -X POST -H "Content-Type: application/json" -d "@${cartridges_path}/tomcat2.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/cartridges

echo "Adding group6c group..."
curl -X POST -H "Content-Type: application/json" -d "@${cartridges_groups_path}/group6c.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/cartridgeGroups

echo "Adding group8c group..."
curl -X POST -H "Content-Type: application/json" -d "@${cartridges_groups_path}/group8c.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/cartridgeGroups

sleep 1

echo "Creating application..."
curl -X POST -H "Content-Type: application/json" -d "@${artifacts_path}/application-definition.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/applications

sleep 1

echo "Deploying application..."
curl -X POST -H "Content-Type: application/json" -d "@${iaas_artifacts_path}/deployment-policy.json" -k -v -u admin:admin https://${host_ip}:${host_port}/api/applications/complex-app/deploy
