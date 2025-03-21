DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS orgs;
DROP TABLE IF EXISTS my_users;
DROP TABLE IF EXISTS roles;
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
    FOREIGN KEY(user_id) REFERENCES my_users(id),
    FOREIGN KEY(org_id) REFERENCES orgs(id),
    FOREIGN KEY(role_id) REFERENCES roles(id)
);

INSERT INTO roles (role) VALUES('admin');
INSERT INTO roles (role) VALUES('stocker');
INSERT INTO roles (role) VALUES('contributer');
INSERT INTO my_users (email) VALUES('thadaniaarush@gmail.com');