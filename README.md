# LeetGuide Project

## Overview
This **LeetGuide Code Project** is a coding interview preparation tool that leverages Reddit discussions to identify the **most talked-about LeetCode problems** and related topics. By analyzing data with advanced aggregation techniques, the project ensures users can efficiently practice **high-impact problems**. Topics and problems are ranked based on a **relevance score**, calculated using their popularity and frequency of discussion across Reddit threads. This tool aggregates approximately **500 problems**, giving users a curated list of essential coding challenges.

---

## Features

### 1. Reddit API Scraping
- Fetches data from popular programming subreddits:
  - `r/leetcode`
  - `r/programming`
  - `r/cscareerquestions`
- Extracts relevant posts, comments, and discussions that mention coding problems and concepts.

### 2. Advanced Aggregation Techniques
- Analyzes scraped data to extract **keywords** and identify **trending topics**.
- Uses a **relevance score** to rank LeetCode problems:
  - Calculated based on the **popularity** (upvotes, comments) and **frequency of mentions** in discussions.
  - Ensures users focus on problems that are highly discussed and impactful for interview preparation.

### 3. Efficient Problem Sorting
- Problems are grouped and sorted into categories, such as:
  - Arrays
  - Dynamic Programming
  - Graphs
  - String Manipulation
- Topics are scored to prioritize essential areas, ensuring users target their practice on high-value problems.

### 4. Scalable Database of Problems
- Maintains a database of approximately **500 LeetCode problems**.
- Continuously updated based on new Reddit discussions.

---

## How It Works

1. **Data Collection**:
   - Scrapes Reddit threads using the **Reddit API**.
   - Filters content to focus on discussions about coding problems.

2. **Data Processing**:
   - Parses posts and comments to extract keywords and problem mentions.
   - Aggregates data for deeper insights using advanced **natural language processing (NLP)** techniques.

3. **Ranking**:
   - Assigns a **relevance score** to each problem based on:
     - Frequency of mentions.
     - Popularity in discussions (e.g., number of upvotes and comments).
   - Sorts problems in descending order of their relevance score.

4. **Output**:
   - Generates a curated list of problems categorized by topics.
   - Provides recommendations to help users practice **efficiently**.

---

## Benefits
- **Time-Saving**: Quickly identify high-priority problems without sifting through random lists.
- **Focused Practice**: Tackle impactful problems based on real-world discussions.
- **Up-to-Date Topics**: Stay relevant with trending problems and concepts in the coding community.

---

## How to Use
1. Clone the repository and set up the environment.
2. Add your Reddit API credentials to the `application.properties` file:
   ```properties
   reddit.client.id=YOUR_CLIENT_ID
   reddit.client.secret=YOUR_CLIENT_SECRET
   reddit.username=YOUR_USERNAME
   reddit.password=YOUR_PASSWORD
   reddit.user.agent=YOUR_APP_NAME/1.0


## Files and Structure

### Key Files
- **main.tf**: Defines the infrastructure resources, including the Droplet and provisioner scripts.
- **variables.tf**: Defines variables like DigitalOcean token, SSH fingerprint, and Droplet size.
- **terraform.tfvars**: Stores your specific variable values (e.g., tokens, fingerprints).
- **README.md**: Documentation for the project.

---

## Prerequisites

Before starting, ensure you have the following:
1. **Terraform Installed**: Install Terraform from [here](https://www.terraform.io/downloads).
2. **DigitalOcean Account**: Create an account at [DigitalOcean](https://www.digitalocean.com/).
3. **SSH Key**: Set up an SSH key pair on your machine and add it to DigitalOcean.
4. **Spring Boot Application**: Build your Spring Boot application as a `.jar` file.

---

## Steps to Use This Project

1. Clone the repository:
   ```bash
   git clone https://github.com/your_username/digitalocean-springboot-terraform.git
   cd digitalocean-springboot-terraform
   ```

2. Modify the `terraform.tfvars` file to include your variables:
   ```hcl
   digitalocean_token = "YOUR_DIGITALOCEAN_API_TOKEN"
   ssh_fingerprint    = "YOUR_SSH_FINGERPRINT"
   ```

3. Add your Spring Boot `.jar` file to the project directory:
   ```bash
   cp /path/to/your-app.jar ./app.jar
   ```

4. Initialize Terraform:
   ```bash
   terraform init
   ```

5. Plan the infrastructure:
   ```bash
   terraform plan
   ```

6. Apply the changes:
   ```bash
   terraform apply
   ```

7. Access your application:
   - Use the IP address from the Terraform output.
   - Configure Nginx to serve your application and a static HTML page if required.

---

## Customization

### Variables
To make the project flexible, modify the variables in the `variables.tf` file:
```hcl
variable "digitalocean_token" {
  description = "DigitalOcean API token"
}

variable "ssh_fingerprint" {
  description = "SSH fingerprint for authentication"
}

variable "region" {
  description = "Region to deploy the Droplet"
  default     = "nyc3"
}

variable "size" {
  description = "Droplet size"
  default     = "s-1vcpu-1gb"
}
```

### Deployment Script
The `user_data` script in `main.tf` installs Java and deploys the Spring Boot application:
```bash
#!/bin/bash
apt update
apt install -y default-jre
mkdir -p /opt/app
echo "${file("app.jar")}" > /opt/app/app.jar
nohup java -jar /opt/app/app.jar &
```

---

