DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS orgs;
DROP TABLE IF EXISTS my_users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS invites;
CREATE TABLE my_users(
    id INT IDENTITY PRIMARY KEY,
    email VARCHAR(64) NOT NULL,
    picture VARCHAR(128)
);


CREATE TABLE roles(
    id INT IDENTITY PRIMARY KEY,
    role VARCHAR(32) NOT NULL
);


CREATE TABLE orgs(
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    image_id TEXT,
);


CREATE TABLE user_roles(
    user_id INT NOT NULL,
    org_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT FK_my_users_user_roles FOREIGN KEY(user_id) REFERENCES my_users(id),
    CONSTRAINT FK_orgs_roles FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_roles_user_roles FOREIGN KEY(role_id) REFERENCES roles(id),
    PRIMARY KEY(user_id, org_id)
);

CREATE TABLE invites(
    email VARCHAR(64),
    org_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT FK_orgs_invites FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_roles_invites FOREIGN KEY(role_id) REFERENCES roles(id),
    PRIMARY KEY(email, org_id)
);

INSERT INTO roles (role) VALUES('admin');
INSERT INTO roles (role) VALUES('stocker');
INSERT INTO roles (role) VALUES('contributer');
INSERT INTO my_users (email) VALUES('thadaniaarush@gmail.com');
INSERT INTO orgs(name, image_url) VALUES('testOrg', 'https://leopardsealimages.blob.core.windows.net/inventorymanagerimages/75033.jpg');
INSERT INTO user_roles (user_id, org_id, role_id) VALUES(1, 1, 1);