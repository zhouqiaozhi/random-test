# SETUP K8S(centos 9)
| ip           | name                                                                              |
|---------------------------|----------------------------------------------------------------------|
| 192.168.50.130            | SSH host address                                                     |
| 192.168.50.131            | SSH port number                                                      |
| 192.168.50.132            | SSH port number                                                      |
##
``` bash
systemctl stop firewalld
systemctl disable firewalld
```
or
``` bash
sudo firewall-cmd --zone=public --permanent --add-port=6443/tcp
...
sudo firewall-cmd --reload
```
##
``` bash
hostnamectl set-hostname k8s-master
```
##
``` bash
sudo dnf install kernel-devel-$(uname -r)
```
##
``` bash
sudo modprobe br_netfilter
sudo modprobe ip_vs
sudo modprobe ip_vs_rr
sudo modprobe ip_vs_wrr
sudo modprobe ip_vs_sh
sudo modprobe overlay

cat > /etc/modules-load.d/kubernetes.conf << EOF
br_netfilter
ip_vs
ip_vs_rr
ip_vs_wrr
ip_vs_sh
overlay
EOF
```
##
``` bash
cat > /etc/sysctl.d/kubernetes.conf << EOF
net.ipv4.ip_forward = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

sysctl --system
```
##
``` bash
sudo swapoff -a

sed -e '/swap/s/^/#/g' -i /etc/fstab
```
##
``` bash
sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```
##
``` bash
sudo dnf -y install containerd.io

sudo sh -c "containerd config default > /etc/containerd/config.toml" ; cat /etc/containerd/config.toml

sudo vim /etc/containerd/config.toml

/SystemdCgroup # set true

sudo systemctl enable --now containerd.service

sudo systemctl reboot
```
##
``` bash
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://pkgs.k8s.io/core:/stable:/v1.30/rpm/
enabled=1
gpgcheck=1
gpgkey=https://pkgs.k8s.io/core:/stable:/v1.30/rpm/repodata/repomd.xml.key
exclude=kubelet kubeadm kubectl cri-tools kubernetes-cni
EOF

dnf install -y kubelet kubeadm kubectl --disableexcludes=kubernetes

systemctl enable --now kubelet.service
```
##
``` bash
sudo kubeadm config images pull
```
##
``` bash
kubeadm init --apiserver-advertise-address=192.168.50.130 --service-cidr=10.96.0.0/12 --pod-network-cidr=10.244.0.0/16
```
if something wrong
``` bash
kebeadm reset

rm -rf /etc/cni/net.d

ipvsadm --clear

rm -rf $HOME/.kube/config
```
##
``` bash
mkdir -p $HOME/.kube

sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
##
``` bash
kubectl create -f https://raw.githubusercontent.com/projectcalico/calico/v3.27.4/manifests/tigera-operator.yaml

wget https://raw.githubusercontent.com/projectcalico/calico/v3.27.4/manifests/custom-resources.yaml

sed -i 's/cidr: 192\.168\.0\.0\/16/cidr: 10.244.0.0\/16/g' custom-resources.yaml

kubectl create -f custom-resources.yaml

reboot
```
##
``` bash
sudo kubeadm token create --print-join-command
```
##
``` bash
kubeadm join 192.168.50.130:6443 --token 2snctn.8gcx4tgun3bjhggu \
        --discovery-token-ca-cert-hash sha256:c0cb2be61a5776e587d43931935a6e71d96eb23f18b910342a90e54b5e6ab444
```
