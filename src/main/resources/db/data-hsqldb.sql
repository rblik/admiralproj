TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK;

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

INSERT INTO projects (name, client_id) VALUES
  ('Office work', 1),
  ('Team management', 1),
  ('Courses', 1),
  ('Freescale Meetings', 1),
  ('Project1', 2),
  ('Project2', 2),
  ('Project1', 3),
  ('Project2', 3),
  ('Project1', 4),
  ('Project2', 4),
  ('Project1', 5),
  ('Project2', 5),
  ('Project1', 6),
  ('Project2', 6);

INSERT INTO work_agreements (start, finish, employee_id, project_id) VALUES
  ('2017-01-01', '2018-01-01', 1, 7),
  ('2016-08-01', '2017-08-01',2, 8),
  ('2016-11-01', '2017-11-01',3, 1),
  ('2017-01-15', '2018-01-15', 4, 10),
  ('2017-01-01', '2018-01-01', 5, 9),
  ('2017-01-01', '2018-01-01', 5, 11);


INSERT INTO work_units (work_date, start, finish, duration, absence_type, work_agreement_id, comment) VALUES
  ('2017-03-19', '09:00:00', '11:00:00', 120, 'ILLNESS', 1, 'Just dumping'),
  ('2017-03-19', '12:00:00', '13:00:00', 60, NULL, 1, 'Just dumping'),
  ('2017-03-20', '09:00:00', '16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-21', '09:00:00', '16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-22', '09:00:00', '16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-23', '09:00:00', '16:00:00', 420, NULL, 1, 'Just dumping'),
  ('2017-03-19', '10:00:00', '17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-20', '10:00:00', '17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-21', '10:00:00', '17:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-22', '10:00:00', '17:30:00', 450, NULL, 2, 'Just dumping'),
  ('2017-03-23', '09:00:00', '16:00:00', 420, NULL, 2, 'Just dumping'),
  ('2017-03-19', '08:00:00', '15:00:00', 420, NULL, 3, 'Just dumping'),
  ('2017-03-20', '08:00:00', '15:00:00', 420, NULL, 3, 'Just dumping'),
  ('2017-03-21', '00:00:00', '23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-22', '00:00:00', '23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-23', '00:00:00', '23:59:59', 1440, 'ILLNESS', 3, 'Just dumping'),
  ('2017-03-19', '10:00:00', '17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-20', '10:00:00', '17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-21', '10:00:00', '17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-22', '10:00:00', '17:30:00', 450, NULL, 4, 'Just dumping'),
  ('2017-03-23', '10:00:00', '17:00:00', 420, NULL, 4, 'Just dumping'),
  ('2017-03-19', '08:00:00', '13:00:00', 300, NULL, 5, 'Just dumping'),
  ('2017-03-20', '10:00:00', '14:00:00', 240, NULL, 5, 'Just dumping'),
  ('2017-03-21', '10:00:00', '12:00:00', 120, NULL, 5, 'Just dumping'),
  ('2017-03-22', '10:00:00', '14:00:00', 240, NULL, 5, 'Just dumping'),
  ('2017-03-23', '00:00:00', '23:59:59', 1440, 'HOLIDAY', 5, 'Just dumping'),
  ('2017-04-19', '15:00:00', '17:00:00', 120, NULL, 6, 'Just dumping'),
  ('2017-04-20', '15:00:00', '18:30:00', 210, NULL, 6, 'Just dumping'),
  ('2017-04-21', '13:00:00', '17:00:00', 240, NULL, 6, 'Just dumping'),
  ('2017-04-22', '08:00:00', '16:30:00', 510, NULL, 6, 'Just dumping'),
  ('2017-04-23', '00:00:00', '23:59:59', 1440, 'HOLIDAY', 6, 'Just dumping');