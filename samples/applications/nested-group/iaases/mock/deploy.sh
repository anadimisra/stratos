#!/bin/bash

export iaas="mock"						#[openstack, ec2, mock, kubernetes]
export host_ip="localhost"

export artifacts_path="../../artifacts"
export cartridges_path="../../../../cartridges/${iaas}"
export cartridges_groups_path="../../../../cartridges-groups"

set -e

# Adding autoscale policy
pushd ${artifacts_path}
echo "Adding autoscale policy..."
curl -X POST -H "Content-Type: application/json" -d @'autoscale-policy.json' -k -v -u admin:admin https://${host_ip}:9443/api/autoscalingPolicies
popd

# Adding cartridges
pushd ${cartridges_path}
	# Adding tomcat cartridge
echo "Adding tomcat cartridge..."
curl -X POST -H "Content-Type: application/json" -d @'tomcat.json' -k -v -u admin:admin https://${host_ip}:9443/api/cartridges

	# Adding tomcat1 cartridge
echo "Adding tomcat1 cartridge..."
curl -X POST -H "Content-Type: application/json" -d @'tomcat1.json' -k -v -u admin:admin https://${host_ip}:9443/api/cartridges

	# Adding tomcat2 cartridge
echo "Adding tomcat2 cartridge..."
curl -X POST -H "Content-Type: application/json" -d @'tomcat2.json' -k -v -u admin:admin https://${host_ip}:9443/api/cartridges
popd

# Adding groups
pushd ${cartridges_path}
	# Adding group6c
echo "Adding group6c group..."
curl -X POST -H "Content-Type: application/json" -d @'group6c.json' -k -v -u admin:admin https://${host_ip}:9443/api/cartridgeGroups
popd

sleep 5

# Creating application
pushd ${artifacts_path}
echo "Creating application..."
curl -X POST -H "Content-Type: application/json" -d @'application-definition.json' -k -v -u admin:admin https://${host_ip}:9443/api/applications
popd

sleep 5

# Deploy application
echo "Deploying application..."
curl -X POST -H "Content-Type: application/json" -d@'artifacts/deployment-policy.json' -k -v -u admin:admin https://${host_ip}:9443/api/applications/myapp1265/deploy

