
terraform {
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = ">= 2.0.0"
    }
  }
}

provider "digitalocean" {
  token = var.digitalocean_token
}

resource "digitalocean_droplet" "springboot" {
  name   = "springboot-app"
  region = "nyc3"          # Choose your preferred region
  size   = "s-1vcpu-1gb"   # Select a droplet size
  image  = "ubuntu-20-04-x64"

  ssh_keys = [var.ssh_fingerprint]

  user_data = <<-EOT
    #!/bin/bash
    apt update -y
    apt install -y default-jre
    mkdir -p /opt/app
    # Replace with actual app.jar download or SCP command
    curl -o /opt/app/app.jar http://example.com/app.jar
    nohup java -jar /opt/app/app.jar &
  EOT
}

output "droplet_ip" {
  value = digitalocean_droplet.springboot.ipv4_address
}
