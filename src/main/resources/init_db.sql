DROP SEQUENCE IF EXISTS dep_seq CASCADE;
DROP SEQUENCE IF EXISTS empl_seq CASCADE;
DROP SEQUENCE IF EXISTS client_seq CASCADE;
DROP SEQUENCE IF EXISTS proj_seq CASCADE;
DROP SEQUENCE IF EXISTS work_seq CASCADE;
DROP SEQUENCE IF EXISTS work_unit_seq CASCADE;
DROP SEQUENCE IF EXISTS address_seq CASCADE;

DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS client_phones;
DROP TABLE IF EXISTS employee_roles;
DROP TABLE IF EXISTS work_units;
DROP TABLE IF EXISTS work_agreements;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS clients;

CREATE SEQUENCE dep_seq START 1000;
CREATE SEQUENCE empl_seq START 1000;
CREATE SEQUENCE client_seq START 1000;
CREATE SEQUENCE proj_seq START 1000;
CREATE SEQUENCE work_seq START 1000;
CREATE SEQUENCE work_unit_seq START 1000;
CREATE SEQUENCE address_seq START 1000;


CREATE TABLE departments
(
  id   INTEGER PRIMARY KEY DEFAULT nextval('dep_seq'),
  name VARCHAR NOT NULL
);

CREATE TABLE employees
(
  id            INTEGER PRIMARY KEY DEFAULT nextval('empl_seq'),
  name          VARCHAR NOT NULL,
  surname       VARCHAR NOT NULL,
  passportId    VARCHAR NOT NULL,
  birthday      TIMESTAMP,
  email         VARCHAR NOT NULL,
  hired         TIMESTAMP           DEFAULT now(),
  active        BOOL                DEFAULT TRUE,
  private_phone VARCHAR,
  company_phone VARCHAR,
  password      VARCHAR NOT NULL,
  department_id INTEGER NOT NULL,
  FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX employee_email_idx_unique ON employees (email);
CREATE UNIQUE INDEX employee_passport_idx_unique ON employees (passportId);

CREATE TABLE employee_roles
(
  employee_id INTEGER NOT NULL,
  role        VARCHAR,
  CONSTRAINT employee_roles_idx UNIQUE (employee_id, role),
  FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);

CREATE TABLE clients
(
  id             INTEGER PRIMARY KEY DEFAULT nextval('client_seq'),
  company_number INTEGER NOT NULL,
  name           VARCHAR NOT NULL
);

CREATE TABLE addresses
(
  id           INTEGER PRIMARY KEY  DEFAULT nextval('address_seq'),
  area         VARCHAR NOT NULL,
  city         VARCHAR NOT NULL,
  street       VARCHAR NOT NULL,
  house_number VARCHAR,
  client_id    INTEGER NOT NULL,
  FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE TABLE client_phones
(
  client_id INTEGER NOT NULL,
  phone     VARCHAR NOT NULL,
  FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE TABLE projects
(
  id        INTEGER PRIMARY KEY DEFAULT nextval('proj_seq'),
  name      VARCHAR,
  client_id INTEGER NOT NULL,
  FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE TABLE work_agreements
(
  id            INTEGER PRIMARY KEY DEFAULT nextval('work_seq'),
  tariff_type   VARCHAR   NOT NULL,
  tariff_amount INTEGER   NOT NULL,
  employee_id   INTEGER   NOT NULL,
  project_id    INTEGER   NOT NULL,
  since         TIMESTAMP DEFAULT now(),
  until         TIMESTAMP,
  FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
  FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

CREATE TABLE work_units
(
  id                INTEGER PRIMARY KEY DEFAULT nextval('work_unit_seq'),
  start             TIMESTAMP NOT NULL,
  finish            TIMESTAMP NOT NULL,
  absence_type      VARCHAR,
  work_agreement_id INTEGER   NOT NULL,
  comment           VARCHAR,
  approved          BOOL,
  FOREIGN KEY (work_agreement_id) REFERENCES work_agreements (id) ON DELETE CASCADE
);