terraform {
    required_providers {
        aws = {
            source = "hashicorp/aws"
            version = "~> 2.70"
        }
    }
}

# AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_DEFAULT_REGION
provider "aws" {}
