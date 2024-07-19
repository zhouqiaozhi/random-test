CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE oauth2_authorization (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    authorized_scopes varchar(1000) DEFAULT NULL,
    attributes blob DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value blob DEFAULT NULL,
    authorization_code_issued_at timestamp DEFAULT NULL,
    authorization_code_expires_at timestamp DEFAULT NULL,
    authorization_code_metadata blob DEFAULT NULL,
    access_token_value blob DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata blob DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    oidc_id_token_value blob DEFAULT NULL,
    oidc_id_token_issued_at timestamp DEFAULT NULL,
    oidc_id_token_expires_at timestamp DEFAULT NULL,
    oidc_id_token_metadata blob DEFAULT NULL,
    refresh_token_value blob DEFAULT NULL,
    refresh_token_issued_at timestamp DEFAULT NULL,
    refresh_token_expires_at timestamp DEFAULT NULL,
    refresh_token_metadata blob DEFAULT NULL,
    user_code_value blob DEFAULT NULL,
    user_code_issued_at timestamp DEFAULT NULL,
    user_code_expires_at timestamp DEFAULT NULL,
    user_code_metadata blob DEFAULT NULL,
    device_code_value blob DEFAULT NULL,
    device_code_issued_at timestamp DEFAULT NULL,
    device_code_expires_at timestamp DEFAULT NULL,
    device_code_metadata blob DEFAULT NULL,
    PRIMARY KEY (id)
);
-- CREATE TABLE oauth2_authorization_consent (
--     registered_client_id varchar(100) NOT NULL,
--     principal_name varchar(200) NOT NULL,
--     authorities varchar(1000) NOT NULL,
--     PRIMARY KEY (registered_client_id, principal_name)
-- );
CREATE TABLE sys_user (
    username varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    password varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    enabled boolean NOT NULL,
    PRIMARY KEY (username)
);
CREATE TABLE authority (
    id INT NOT NULL AUTO_INCREMENT,
    username varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    authority varchar(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES sys_user(username)
);

INSERT INTO oauth2_registered_client(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings) VALUES('94b37d23-9020-4266-a904-beba0af34af9', 'gateway-id', CURRENT_TIMESTAMP(), '$2a$10$t9RJN7BujjuXMcgPlSYn3OtJFcB/0pd07alH/84wV6GW.JSFkvry6', NULL, 'api-gateway', 'client_secret_basic', 'refresh_token,authorization_code', 'http://api-gateway:8080/login/oauth2/code/oidc-client', '', 'openid,profile', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');
insert into sys_user(username, password, enabled) values ("user", "$2a$10$FOMlPVhEqvNlPLHL1QzRau9lz7jzonk2xyErHmiTP9n/iM20pEcRm", true); -- user: user
insert into sys_user(username, password, enabled) values ("admin", "$2a$10$PuZcxxxxt.o4x/1DBOu.j.sbq7UjORxAmXe/Att7EacgiWyQFLy6W", true); -- admin: admin
insert into authority (username, authority) values ("admin", "ROLE_ADMIN");
insert into authority (username, authority) values ("admin", "ROLE_USER");
insert into authority (username, authority) values ("user", "ROLE_USER");