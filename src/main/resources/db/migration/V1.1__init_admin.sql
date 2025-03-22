insert into profile(id,name,username,password,status,visible,created_date)
values ('fa8c6617-33fb-46ad-8844-4327a229ac8f','Sanjar','sanjar902105@gmail.com',
        '$2a$12$rx4JuQVjdLXJ8jgeTySkvezWKuQ8jNZJBijNgo9IFQj3TPOoxtcB2','ACTIVE',true,now());

INSERT INTO profile_role (profile_id, role, created_date)
SELECT id, 'ROLE_ADMIN', now() FROM profile WHERE username = 'sanjar902105@gmail.com'
UNION ALL
SELECT id, 'ROLE_USER', now() FROM profile WHERE username = 'sanjar902105@gmail.com';