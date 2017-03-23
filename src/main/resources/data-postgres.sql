TRUNCATE departments, employees, employee_roles, clients, addresses, client_phones, projects, work_agreements, tariffs, work_units RESTART IDENTITY;

INSERT INTO departments (name) VALUES
  ('Java'),
  ('MS DB'),
  ('Oracle'),
  ('BI'),
  ('Data Science'),
  ('Sales'),
  ('Finances'),
  ('Courses'),
  ('Human Resources');

INSERT INTO employees (name, surname, passportid, birthday, email, hired, private_phone, password, department_id) VALUES
  ('Name1', 'Surname1', 234472323, '1980-07-11', 'name1@gmail.com', '2016-03-02', '050-1111111', 'Qwerty123', 1),
  ('Name2', 'Surname2', 321321222, '1977-10-11', 'name2@gmail.com', '2016-01-25', '041-3211412', 'Qwerty123', 3),
  ('Name3', 'Surname3', 224535323, '1987-03-21', 'name3@gmail.com', '2015-05-01', '053-1231232', 'Qwerty123', 7),
  ('Name4', 'Surname4', 133222888, '1982-11-02', 'name4@gmail.com', '2015-04-30', '055-7656222', 'Qwerty123', 5),
  ('Name5', 'Surname5', 125225322, '1981-08-25', 'name5@gmail.com', '2014-12-21', '053-0023341', 'Qwerty123', 5);

INSERT INTO employee_roles (employee_id, role) VALUES
  (1, 'ROLE_USER'),
  (2, 'ROLE_USER'),
  (3, 'ROLE_USER'),
  (3, 'ROLE_ADMIN'),
  (4, 'ROLE_USER'),
  (5, 'ROLE_USER');

INSERT INTO clients (company_number, name) VALUES
  (123123123, 'Naya'),
  (234234234, 'Bezeq'),
  (867592006, 'Y-Net'),
  (126030400, 'CheckPoint'),
  (266099396, 'LeumiBank'),
  (384695480, 'Elbit');

INSERT INTO addresses (area, city, street, house_number, client_id) VALUES
  ('Center', 'Herzliya', 'Hanadiv', '71', 1),
  ('Tel Aviv', 'Tel Aviv', 'Azrieli Center', 2, 2),
  ('Tel Aviv', 'Tel Aviv', 'Kremenetsky', 12, 3),
  ('Tel Aviv', 'Ramat Gan', 'HaSolelim', 5, 4),
  ('Jerusalem', 'Jerusalem', 'Agudat Sport Hapoel', 1, 5),
  ('Haifa', 'Haifa', 'Advanced Technology Centre', NULL, 6);

INSERT INTO client_phones (client_id, phone) VALUES
  (1, '09-7465005'),
  (1, '09-7465006'),
  (2, '03-7278199'),
  (2, '03-9203170'),
  (2, '03-9203008'),
  (3, '03-6082222'),
  (3, '03-6933933'),
  (4, '03-7534555'),
  (4, '03-7534556'),
  (5, '03-9545522'),
  (5, '03-9544555'),
  (6, '04-8316663'),
  (6, '04-8315315');

INSERT INTO projects (name, client_id) VALUES
  ('Office work', 1),
  ('Team management', 1),
  ('Courses', 1),
  ('Freescale Meetings', 1),
  ('Consulting', 2),
  ('Developing', 2),
  ('Consulting', 3),
  ('Developing', 3),
  ('Consulting', 4),
  ('Developing', 4),
  ('Consulting', 5),
  ('Developing', 5),
  ('Consulting', 6),
  ('Developing', 6);

INSERT INTO tariffs (tariff_type, tariff_amount) VALUES
  ('HOURLY', '100'),
  ('MONTHLY', '20000'),
  ('HOURLY', '100'),
  ('HOURLY', '80'),
  ('HOURLY', '150'),
  ('HOURLY', '150');

INSERT INTO work_agreements (employee_id, project_id, since, until, tariff_id) VALUES
  (1, 7, '2016-05-11', '2017-05-11', 1),
  (2, 8, '2017-01-10', '2018-01-10', 2),
  (3, 1, '2015-09-22', '2018-09-22', 3),
  (4, 10, '2016-08-01', '2018-08-01', 4),
  (5, 9, '2016-03-03', '2018-03-03', 5),
  (5, 11, '2016-07-12', '2018-07-12', 6);

INSERT INTO work_units (start, finish, duration, absence_type, work_agreement_id, comment) VALUES
  ('2017-03-19 09:00:00', '2017-03-19 16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-20 09:00:00', '2017-03-20 16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-21 09:00:00', '2017-03-21 16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-22 09:00:00', '2017-03-22 16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-23 09:00:00', '2017-03-23 16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-19 10:00:00', '2017-03-19 17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-20 10:00:00', '2017-03-20 17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-21 10:00:00', '2017-03-21 17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-22 10:00:00', '2017-03-22 17:30:00', 450, NULL, 2, 'Just dumping'),
  ('2017-03-23 09:00:00', '2017-03-23 16:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-19 08:00:00', '2017-03-19 15:00:00', 420, NULL, 3, 'Just dumping'),
  ('2017-03-20 08:00:00', '2017-03-20 15:00:00', 420, NULL, 3, 'Just dumping'),
  ('2017-03-21 00:00:00', '2017-03-21 23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-22 00:00:00', '2017-03-22 23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-23 00:00:00', '2017-03-23 23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-19 10:00:00', '2017-03-19 17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-20 10:00:00', '2017-03-20 17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-21 10:00:00', '2017-03-21 17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-22 10:00:00', '2017-03-22 17:30:00', 450, NULL, 4, 'Just dumping'),
  ('2017-03-23 10:00:00', '2017-03-23 17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-19 08:00:00', '2017-03-19 13:00:00', 300, NULL, 5, 'Just dumping'),
  ('2017-03-20 10:00:00', '2017-03-20 14:00:00', 240, NULL, 5, 'Just dumping'),
  ('2017-03-21 10:00:00', '2017-03-21 12:00:00', 120, NULL, 5, 'Just dumping'),
  ('2017-03-22 10:00:00', '2017-03-22 14:00:00', 240, NULL, 5, 'Just dumping'),
  ('2017-03-23 00:00:00', '2017-03-23 23:59:59', 1440, 'HOLIDAY', 5, 'Just dumping'),
  ('2017-03-19 15:00:00', '2017-03-19 17:00:00', 120, NULL, 6, 'Just dumping'),
  ('2017-03-20 15:00:00', '2017-03-20 18:30:00', 210, NULL, 6, 'Just dumping'),
  ('2017-03-21 13:00:00', '2017-03-21 17:00:00', 240, NULL, 6, 'Just dumping'),
  ('2017-03-22 08:00:00', '2017-03-22 16:30:00', 510, NULL, 6, 'Just dumping'),
  ('2017-03-23 00:00:00', '2017-03-23 23:59:59', 1440, 'HOLIDAY', 6, 'Just dumping');