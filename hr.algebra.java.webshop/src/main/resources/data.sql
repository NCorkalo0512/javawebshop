INSERT INTO  users (username, password, enabled) VALUES ('user','$2a$10$CHrLexsxBQPS9C.g4t3hruS.OalKaai4pvrPHjYnGST.M80Whndlq', 'true');/*user-password*/
INSERT INTO  users (username, password, enabled) VALUES ('admin','$2a$10$8QeyN/XkJ6VntiETgaWB/Opiul86rfk0e0uZ8Bkjb8Dz0eQbRri36', 'true'); /*admin-password*/

INSERT INTO authorities (id_users, role) VALUES(1, 'USER');
INSERT INTO authorities (id_users, role) VALUES(2, 'ADMIN');