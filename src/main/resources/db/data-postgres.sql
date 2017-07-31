TRUNCATE default_choices, departments, monthly_standards, datelocks, employees, employee_roles, clients, addresses, client_phones, projects, tariffs, work_agreements, work_units RESTART IDENTITY;

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

INSERT INTO employees (name, surname, passport_id, employee_number, birthday, email, private_phone, password, department_id) VALUES
  ('Name1', 'Surname1', 234472323, 1001, '1980-07-11', 'name1@gmail.com', '050-1111111',
   '$2a$10$zflDeCzgkCRgC0MgJlNGPOILOAYO6A1WbLFuhN6zjfQE0JF1/S3Vq', 1),
  ('Name2', 'Surname2', 321321222, 1002, '1977-10-11', 'name2@gmail.com', '041-3211412',
   '$2a$10$zflDeCzgkCRgC0MgJlNGPOILOAYO6A1WbLFuhN6zjfQE0JF1/S3Vq', 3),
  ('Name3', 'Surname3', 224535323, 1003, '1987-03-21', 'name3@gmail.com', '053-1231232',
   '$2a$10$zflDeCzgkCRgC0MgJlNGPOILOAYO6A1WbLFuhN6zjfQE0JF1/S3Vq', 7),
  ('Name4', 'Surname4', 133222888, 1004, '1982-11-02', 'name4@gmail.com', '055-7656222',
   '$2a$10$zflDeCzgkCRgC0MgJlNGPOILOAYO6A1WbLFuhN6zjfQE0JF1/S3Vq', 5),
  ('Name5', 'Surname5', 125225322, 1005, '1981-08-25', 'name5@gmail.com', '053-0023341',
   '$2a$10$zflDeCzgkCRgC0MgJlNGPOILOAYO6A1WbLFuhN6zjfQE0JF1/S3Vq', 5);

INSERT INTO employee_roles (employee_id, role) VALUES
  (1, 'ROLE_USER'),
  (2, 'ROLE_USER'),
  (3, 'ROLE_USER'),
  (5, 'ROLE_ADMIN'),
  (4, 'ROLE_USER'),
  (5, 'ROLE_USER');

INSERT INTO clients (company_number, client_number, name) VALUES
  (123123123, 5001, 'Naya'),
  (234234234, 5002, 'Bezeq'),
  (867592006, 5003, 'Y-Net'),
  (126030400, 5004, 'CheckPoint'),
  (266099396, 5005, 'LeumiBank'),
  (384695480, 5006, 'Elbit');

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

INSERT INTO tariffs (amount, currency, type) VALUES
  (15000, 'SHEKEL', 'MONTH'),
  (18000, 'SHEKEL', 'MONTH'),
  (18000, 'SHEKEL', 'MONTH'),
  (20000, 'SHEKEL', 'MONTH'),
  (300, 'SHEKEL', 'HOUR'),
  (350, 'SHEKEL', 'HOUR'),
  (350, 'SHEKEL', 'HOUR'),
  (250, 'SHEKEL', 'HOUR'),
  (200, 'SHEKEL', 'HOUR'),
  (500, 'SHEKEL', 'HOUR'),
  (100, 'DOLLAR', 'HOUR'),
  (150, 'DOLLAR', 'HOUR'),
  (300, 'SHEKEL', 'HOUR'),
  (50000, 'SHEKEL', 'FIX'),
  (350, 'SHEKEL', 'HOUR'),
  (250, 'SHEKEL', 'HOUR'),
  (15000, 'SHEKEL', 'MONTH'),
  (500, 'SHEKEL', 'HOUR'),
  (200, 'SHEKEL', 'HOUR'),
  (100, 'DOLLAR', 'HOUR');

INSERT INTO projects (name, client_id, tariff_id) VALUES
  ('Office work', 1, 1),
  ('Team management', 1, 2),
  ('Courses', 1, 3),
  ('Freescale Meetings', 1, 4),
  ('Project1', 2, 5),
  ('Project2', 2, 6),
  ('Project1', 3, 7),
  ('Project2', 3, 8),
  ('Project1', 4, 9),
  ('Project2', 4, 10),
  ('Project1', 5, 11),
  ('Project2', 5, 12),
  ('Project1', 6, 13),
  ('Project2', 6, 14);

INSERT INTO work_agreements (active, employee_id, project_id, tariff_id) VALUES
  (TRUE , 1, 7, 15),
  (TRUE ,2, 8, 16),
  (TRUE ,3, 1, 17),
  (TRUE , 4, 10, 18),
  (TRUE , 5, 9, 19),
  (TRUE , 5, 11, 20);

INSERT INTO month_infos (month, year, employee_id, locked) VALUES
  (1, 2017, 1, TRUE),
  (2, 2017, 1, TRUE),
  (3, 2017, 1, TRUE),
  (1, 2017, 2, TRUE),
  (2, 2017, 2, TRUE),
  (3, 2017, 2, TRUE),
  (1, 2017, 3, TRUE),
  (2, 2017, 3, TRUE),
  (3, 2017, 3, TRUE),
  (1, 2017, 4, TRUE),
  (2, 2017, 4, TRUE),
  (3, 2017, 4, TRUE),
  (1, 2017, 5, TRUE),
  (2, 2017, 5, TRUE),
  (3, 2017, 5, TRUE);

INSERT INTO work_units (work_date, start, finish, duration, work_agreement_id, comment) VALUES
  ('2017-03-19', '09:00:00', '11:00:00', 120, 1, 'Just dumping'),
  ('2017-03-19', '12:00:00', '13:00:00', 60,  1, 'Just dumping'),
  ('2017-03-20', '09:00:00', '16:00:00', 420, 1, 'Just dumping'),
  ('2017-03-21', '09:00:00', '16:00:00', 420, 1, 'Just dumping'),
  ('2017-03-22', '09:00:00', '16:00:00', 420, 1, 'Just dumping'),
  ('2017-03-23', '09:00:00', '16:00:00', 420, 1, 'Just dumping'),
  ('2017-03-19', '10:00:00', '17:00:00', 420, 2, 'Just dumping'),
  ('2017-03-20', '10:00:00', '17:00:00', 420, 2, 'Just dumping'),
  ('2017-03-21', '10:00:00', '17:00:00', 420, 2, 'Just dumping'),
  ('2017-03-22', '10:00:00', '17:30:00', 450, 2, 'Just dumping'),
  ('2017-03-23', '09:00:00', '16:00:00', 420, 2, 'Just dumping'),
  ('2017-03-19', '08:00:00', '15:00:00', 420, 3, 'Just dumping'),
  ('2017-03-20', '08:00:00', '15:00:00', 420, 3, 'Just dumping'),
  ('2017-03-21', '00:08:00', '15:00:00', 420, 3, 'Just dumping'),
  ('2017-03-22', '00:08:00', '15:00:00', 420, 3, 'Just dumping'),
  ('2017-03-23', '00:08:00', '15:00:00', 420, 3, 'Just dumping'),
  ('2017-03-19', '10:00:00', '17:00:00', 420, 4, 'Just dumping'),
  ('2017-03-20', '10:00:00', '17:00:00', 420, 4, 'Just dumping'),
  ('2017-03-21', '10:00:00', '17:00:00', 420, 4, 'Just dumping'),
  ('2017-03-22', '10:00:00', '17:30:00', 450, 4, 'Just dumping'),
  ('2017-03-23', '10:00:00', '17:00:00', 420, 4, 'Just dumping'),
  ('2017-03-19', '08:00:00', '13:00:00', 300, 5, 'Just dumping'),
  ('2017-03-20', '10:00:00', '14:00:00', 240, 5, 'Just dumping'),
  ('2017-03-21', '10:00:00', '12:00:00', 120, 5, 'Just dumping'),
  ('2017-03-22', '10:00:00', '14:00:00', 240, 5, 'Just dumping'),
  ('2017-03-23', '00:08:00', '15:00:00', 420, 5, 'Just dumping'),
  ('2017-04-19', '15:00:00', '17:00:00', 120, 6, 'Just dumping'),
  ('2017-04-20', '15:00:00', '18:30:00', 210, 6, 'Just dumping'),
  ('2017-04-21', '13:00:00', '17:00:00', 240, 6, 'Just dumping'),
  ('2017-04-22', '08:00:00', '16:30:00', 510, 6, 'Just dumping'),
  ('2017-04-23', '08:00:00', '15:00:00', 420, 6, 'Just dumping');