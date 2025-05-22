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
    id INT IDENTITY PRIMARY KEY,
    user_id INT NOT NULL,
    org_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT FK_my_users_user_roles FOREIGN KEY(user_id) REFERENCES my_users(id),
    CONSTRAINT FK_orgs_roles FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_roles_user_roles FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE invites(
    id INT IDENTITY PRIMARY KEY,
    user_id INT NOT NULL,
    org_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT FK_users_invites FOREIGN KEY(user_id) REFERENCES my_users(id),
    CONSTRAINT FK_orgs_invites FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_roles_invites FOREIGN KEY(role_id) REFERENCES roles(id)
);
CREATE TABLE locations(
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    org_id INT NOT NULL,
    barcode VARCHAR(64),
    description VARCHAR(512),
    image_url VARCHAR(256),
    CONSTRAINT FK_orgs_locations FOREIGN KEY(org_id) REFERENCES orgs(id)
);
CREATE TABLE box_sizes(
    id INT IDENTITY PRIMARY KEY,
    size VARCHAR(16)
);
CREATE TABLE boxes(
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    org_id INT NOT NULL,
    barcode VARCHAR(64),
    location_id INT,
    size_id INT NOT NULL DEFAULT(1),
    image_url VARCHAR(256),
    CONSTRAINT FK_orgs_boxes FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_locations_boxes FOREIGN KEY(location_id) REFERENCES locations(id),
    CONSTRAINT FK_sizes_boxes FOREIGN KEY(size_id) REFERENCES box_sizes(id)
);
CREATE TABLE items(
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    org_id INT NOT NULL,
    barcode VARCHAR(64),
    description VARCHAR(512),
    box_id INT,
    quantity INT NOT NULL,
    alert INT NOT NULL,
    image_url VARCHAR(256),
    CONSTRAINT FK_orgs_items FOREIGN KEY(org_id) REFERENCES orgs(id),
    CONSTRAINT FK_boxes_items FOREIGN KEY(box_id) REFERENCES boxes(id)
);


INSERT INTO roles (role) VALUES('admin');
INSERT INTO roles (role) VALUES('stocker');
INSERT INTO roles (role) VALUES('contributer');
INSERT INTO box_sizes (size) VALUES('big');
INSERT INTO box_sizes (size) VALUES('medium');
INSERT INTO box_sizes (size) VALUES('small');
INSERT INTO my_users (email) VALUES('thadaniaarush@gmail.com');
INSERT INTO orgs(name, image_url) VALUES('testOrg', 'https://leopardsealimages.blob.core.windows.net/inventorymanagerimages/75033.jpg');
INSERT INTO user_roles (user_id, org_id, role_id) VALUES(1, 1, 1);