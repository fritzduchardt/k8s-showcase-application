# Kubernetes Showcase Application

This project contains a Java Spring Boot Application that is designed to show case the core features of Kubernetes:

- Deployments including scaling
- Services
- Ingress
- Config Maps
- Secrets
- Persistent Volumes
- VolumeSnapshots

## Prerequisites:

- Metrics Exporter
- Ingress Controller

## Usage

``` shell script
# Let's get things started by createing a Deployment with environment variables from a ConfigMap and a Secret as well as a ConfigMap mounted as a volume.
kubectl apply -f src/main/k8s/deployment.yaml

# The 2 Pods of this deployment will fail to start, since the required ConfigMaps don't exist.
# Let's install the ConfigMap for the mounted volume:
kubectl apply -f src/main/k8s/configmap-from-file.yaml

# The Pods will still not start, because ConfigMap and Secret for the environment variables are missing.
# Let's install those
kubectl apply -f src/main/k8s/configmap-from-literal.yaml
kubectl apply -f src/main/k8s/secret-from-literal.yaml  

# Now, the Pod should be running. Let's create a service to connect to the Pod locally from a stable domain name
kubectl apply -f src/main/k8s/service.yaml

# Let's open a local connection to the service..
kubectl port-forward service/k8sshowcase 8080
# ..and query the hostname endpoint like this:
curl localhost:8080/hostname

# Let's create an ingress entity to connect to the Service from outside the cluster
kubectl apply -f src/main/k8s/ingress.yaml
# Obtain the external cluster IP address like this..
kubectl get service -l component=controller,release=nginx-ingress
# ..and call the service externally like follows. *Observe that the Pod hostname changes, because calls are distributed between Pods.**
curl [external-ip-address]/k8sshowcase/hostname

# Let's query environment variables that were set with the ConfigMap
curl localhost:8080/env/MESSAGE
# ..and the environment variable set with the Secret
curl localhost:8080/env/SECRET_MESSAGE

# Now, let's edit the ConfigMap and see whether this has any effect on the deployment:
curl localhost:8080/env/MESSAGE
# It does NOT! So, let's delete the Pods to get them restarted:
kubectl delete po -l app=k8sshowcase
# Once everything has redeployed, check again and verify that changes have propagated to the environment variables:
curl localhost:8080/env/MESSAGE

# Let's have a look at the ConfigMap that has been mounted to a volume:
curl localhost:8080/list/configmap-from-file
curl localhost:8080/content/configmap-from-file/eggs.txt

# Now, let's make a change to the ConfigMap. Give it a couple of seconds and notice that for mounted ConfigMaps and Secrets changes are propagated without redeployment.

# Next, let's change our deployment to mount a PersistentVolume
kubectl apply -f src/main/k8s/deployment-with-persistent-volume.yaml

# You will notice that the Pods remain in "Pending" status. This is because they can't find their PeristentVolumeClaim. The claim gets created like this:
kubectl apply -f src/main/k8s/deployment-with-persistent-volume.yaml
# Deployment might still fail due to the PersistentVolume being mounted from Pods on different nodes. If this happens comment in the affinity rules in the deployment.yaml and try again.

# Once both Pods are running exec into one of them..
kubectl exec -it [name-of-the-pod] -- bash
.. and create a file in the mounted directory:
echo some-new-content > /persistent-volume/new-content.txt

# Let's now create a VolumeSnapshot
kubectl apply -f src/main/k8s/volume-snapshot.yaml
# Wait until it is ready to use:
kubectl get volumesnapshots.snapshot.storage.k8s.io -w
# Then create a PersistentVolumeClaim that uses the snapshot as a datasource
kubectl apply -f src/main/k8s/persistentvolumeclaim-snapshot.yaml
# Now, delete the original deployment
kubectl delete deploy k8sshowcase
# And recreate the deployment while pointing it to the new claim:
kubectl apply -f src/main/k8s/deployment-with-persistent-volume-from-snapshot.yaml
# Finally, verify that the previously created file has been restored:
curl localhost:8080/content/persistent-volume/new-content.txt
```
