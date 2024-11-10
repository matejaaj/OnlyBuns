INSERT INTO public.roles (id, name)
VALUES (1, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.roles (id, name)
VALUES (2, 'ROLE_USER')
ON CONFLICT (id) DO NOTHING;

