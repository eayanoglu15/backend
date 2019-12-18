alter table public.users
add column image varchar(10485759) default 'data:image/png;base64,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';