ALTER TABLE topicos
ADD CONSTRAINT unq_titulo_mensaje UNIQUE (titulo(255), mensaje(255));
