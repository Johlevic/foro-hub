-- 1. Elimina la columna estado (solo si ya no la vas a usar)
ALTER TABLE topicos DROP COLUMN estado;

-- 2. Agrega la columna "activo" como tinyint con valor por defecto en 1
ALTER TABLE topicos ADD COLUMN activo TINYINT(1) DEFAULT 1 AFTER curso;
