CREATE TABLE IF NOT EXISTS organizations ( id VARCHAR(50) NOT NULL, org_name VARCHAR(250) NOT NULL, PRIMARY KEY(id) );
CREATE TABLE IF NOT EXISTS spaces ( org_name VARCHAR(250) NOT NULL, space_name VARCHAR(250) NOT NULL, PRIMARY KEY(org_name, space_name) );
CREATE TABLE IF NOT EXISTS application_detail ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, organization VARCHAR(100), space VARCHAR(100), app_id VARCHAR(50), app_name VARCHAR(100), buildpack VARCHAR(50), image VARCHAR(250), stack VARCHAR(25), running_instances INT, total_instances INT, memory_used MEDIUMINT, disk_used MEDIUMINT, urls VARCHAR(2000), last_pushed DATETIME, last_event VARCHAR(50), last_event_actor VARCHAR(100), last_event_time DATETIME, requested_state VARCHAR(25), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS service_instance_detail ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, organization VARCHAR(100), space VARCHAR(100), service_instance_id VARCHAR(50), service_name VARCHAR(100), service VARCHAR(100), description VARCHAR(1000), plan VARCHAR(50), type VARCHAR(30), bound_applications VARCHAR(2000), last_operation VARCHAR(50), last_updated DATETIME, dashboard_url VARCHAR(250), requested_state VARCHAR(25), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS application_policy ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, id VARCHAR(50), operation VARCHAR(25), description VARCHAR(1000), state VARCHAR(25), options VARCHAR(2000), organization_whitelist VARCHAR(2000), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS service_instance_policy ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, id VARCHAR(50), operation VARCHAR(25), description VARCHAR(1000), options VARCHAR(2000), organization_whitelist VARCHAR(2000), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS query_policy ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, id VARCHAR(50), description VARCHAR(1000), queries VARCHAR(2000), email_notification_template VARCHAR(2000), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS application_relationship ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, organization VARCHAR(100), space VARCHAR(100), app_id VARCHAR(50), app_name VARCHAR(100), service_instance_id VARCHAR(50), service_name VARCHAR(100), service_plan VARCHAR(50), service_type VARCHAR(30), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS historical_record ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, transaction_datetime DATETIME, action_taken VARCHAR(20), organization VARCHAR(100), space VARCHAR(100), app_id VARCHAR(50), service_instance_id VARCHAR(50), type VARCHAR(20), name VARCHAR(300), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS space_users ( pk MEDIUMINT NOT NULL AUTO_INCREMENT, organization varchar(100), space varchar(100), auditors VARCHAR(2000), managers VARCHAR(2000), developers VARCHAR(2000), PRIMARY KEY(pk) );
CREATE TABLE IF NOT EXISTS time_keeper ( collection_time DATETIME );
CREATE OR REPLACE VIEW service_bindings AS select ad.pk, ad.organization, ad.space, ad.app_id, ar.service_instance_id, ad.app_name, ad.buildpack, ad.image, ad.stack, ad.running_instances, ad.total_instances, ad.memory_used, ad.disk_used, ad.urls, ad.last_pushed, ad.last_event, ad.last_event_actor, ad.last_event_time, ad.requested_state from application_detail ad left join application_relationship ar on ad.app_id = ar.app_id;