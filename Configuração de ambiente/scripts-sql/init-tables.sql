-- Papeis no sistema
insert into roles (role_id, name) values (1, 'ADMIN');
insert into roles (role_id, name) values (2, 'DOCENTE');
insert into roles (role_id, name) values (3, 'DISCENTE');

-- Para configuração inicial destes usuários a senha é o mesmo que o login
insert into usuarios (id, ativo, contato, cpf, email, nome, login, senha) values (1, true, '000000000', '00000000000', 'jodeilsonweslley@gmail.com', 'jodeilson', 'admin', '$2a$10$TLCkgUN7Xy4vFKuWSZiPKe1INUNDDto5MboZU67TNG5rgpheZ7XjS');
insert into usuarios (id, ativo, contato, cpf, email, nome, login, senha) values (2, true, '000000001', '00000000001', 'email-teste', 'josenalde', 'docente', '$2a$10$VXjcOn2.flnAz4xDWHAKl.BWQzKxgQWzU/Bee.eC7yuLvdkBIVnL6');
insert into usuarios (id, ativo, contato, cpf, email, nome, login, senha) values (3, true, '000000002', '00000000002', 'email-teste-discente', 'gustavo', 'gustavo', '$2a$10$xoeEGjK2ATI2BED3wpb6dessyLXU8MTgG8cENlBRIGtFTIlaLSaTG');

-- Relacionando os papeis com os usuários
insert into usuarios_roles (usuario_id, role_id) values (1,1);
insert into usuarios_roles (usuario_id, role_id) values (2,2);
insert into usuarios_roles (usuario_id, role_id) values (3,3);

