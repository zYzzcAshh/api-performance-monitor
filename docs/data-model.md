# Data Model

## users

| field | type |
|------|------|
id | int
username | string
password_hash | string
created_at | timestamp

## endpoints

| field | type |
|------|------|
id | int
user_id | int
url | url
name | string
intervalSeconds | long
createdAt | timestamp

## monitoring_results

| field | type |
|------|------|
endpoint_id | string
timestamp | timestamp
latency | long
status_code | int