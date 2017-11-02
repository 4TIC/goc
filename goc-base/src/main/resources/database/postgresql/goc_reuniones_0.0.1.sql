SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = warning;

CREATE SCHEMA goc;

CREATE FUNCTION goc.quita_acentos(txt CHARACTER VARYING) RETURNS CHARACTER VARYING
LANGUAGE plpgsql
AS $$
BEGIN
  RETURN translate(lower(txt), 'áéíóúàèìòùäëïöüâêîôû', 'aeiouaeiouaeiouaeiou');
END;
$$;


ALTER FUNCTION goc.quita_acentos(txt CHARACTER VARYING ) OWNER TO goc;

SET default_tablespace = '';

SET default_with_oids = FALSE;

--
-- Name: goc_cargos; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_cargos (
  id         DOUBLE PRECISION NOT NULL,
  nombre     CHARACTER VARYING(400),
  nombre_alt CHARACTER VARYING(400)
);


ALTER TABLE goc.goc_cargos OWNER TO goc;

--
-- Name: goc_claves; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_claves (
  id            DOUBLE PRECISION NOT NULL,
  clave         CHARACTER VARYING(100),
  clave_alt     CHARACTER VARYING(100),
  estado        NUMERIC(1, 0),
  id_descriptor DOUBLE PRECISION
);


ALTER TABLE goc.goc_claves OWNER TO goc;

--
-- Name: goc_descriptores; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_descriptores (
  id              DOUBLE PRECISION NOT NULL,
  descriptor      CHARACTER VARYING(100),
  descriptor_alt  CHARACTER VARYING(100),
  descripcion     CHARACTER VARYING(200),
  descripcion_alt CHARACTER VARYING(200),
  estado          NUMERIC(1, 0)
);


ALTER TABLE goc.goc_descriptores OWNER TO goc;

--
-- Name: goc_descriptores_tipos_organo; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_descriptores_tipos_organo (
  id             DOUBLE PRECISION NOT NULL,
  descriptor_id  DOUBLE PRECISION NOT NULL,
  tipo_organo_id DOUBLE PRECISION NOT NULL
);


ALTER TABLE goc.goc_descriptores_tipos_organo OWNER TO goc;

--
-- Name: goc_miembros; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_miembros (
  id         DOUBLE PRECISION       NOT NULL,
  nombre     CHARACTER VARYING(500) NOT NULL,
  email      CHARACTER VARYING(200) NOT NULL,
  organo_id  DOUBLE PRECISION       NOT NULL,
  cargo_id   DOUBLE PRECISION,
  persona_id DOUBLE PRECISION       NOT NULL
);


ALTER TABLE goc.goc_miembros OWNER TO goc;

--
-- Name: goc_organos; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_organos (
  id             DOUBLE PRECISION       NOT NULL,
  nombre         CHARACTER VARYING(100) NOT NULL,
  tipo_organo_id DOUBLE PRECISION       NOT NULL,
  creador_id     DOUBLE PRECISION       NOT NULL,
  fecha_creacion TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  inactivo       BOOLEAN DEFAULT FALSE,
  nombre_alt     CHARACTER VARYING(400)
);


ALTER TABLE goc.goc_organos OWNER TO goc;

--
-- Name: goc_organos_autorizados; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_organos_autorizados (
  id             DOUBLE PRECISION        NOT NULL,
  persona_id     DOUBLE PRECISION        NOT NULL,
  persona_nombre CHARACTER VARYING(1000) NOT NULL,
  organo_id      CHARACTER VARYING(100)  NOT NULL,
  organo_externo BOOLEAN                 NOT NULL
);


ALTER TABLE goc.goc_organos_autorizados OWNER TO goc;

--
-- Name: goc_organos_invitados; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_organos_invitados (
  id             DOUBLE PRECISION        NOT NULL,
  persona_id     DOUBLE PRECISION        NOT NULL,
  persona_nombre CHARACTER VARYING(1000) NOT NULL,
  persona_email  CHARACTER VARYING(1000) NOT NULL,
  organo_id      CHARACTER VARYING(100)  NOT NULL
);


ALTER TABLE goc.goc_organos_invitados OWNER TO goc;

--
-- Name: goc_organos_reuniones; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_organos_reuniones (
  id                DOUBLE PRECISION       NOT NULL,
  reunion_id        DOUBLE PRECISION       NOT NULL,
  organo_nombre     CHARACTER VARYING(400),
  tipo_organo_id    DOUBLE PRECISION,
  externo           BOOLEAN,
  organo_id         CHARACTER VARYING(100) NOT NULL,
  organo_nombre_alt CHARACTER VARYING(400)
);


ALTER TABLE goc.goc_organos_reuniones OWNER TO goc;

--
-- Name: goc_organos_reuniones_miembros; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_organos_reuniones_miembros (
  id                    DOUBLE PRECISION       NOT NULL,
  organo_reunion_id     DOUBLE PRECISION       NOT NULL,
  organo_externo        BOOLEAN                NOT NULL,
  reunion_id            DOUBLE PRECISION       NOT NULL,
  organo_id             CHARACTER VARYING(400) NOT NULL,
  nombre                CHARACTER VARYING(500) NOT NULL,
  email                 CHARACTER VARYING(200) NOT NULL,
  asistencia            BOOLEAN DEFAULT FALSE  NOT NULL,
  cargo_id              CHARACTER VARYING(400) NOT NULL,
  cargo_nombre          CHARACTER VARYING(400) NOT NULL,
  suplente_id           DOUBLE PRECISION,
  suplente_nombre       CHARACTER VARYING(1000),
  asistencia_confirmada BOOLEAN,
  suplente_email        CHARACTER VARYING(200),
  miembro_id            CHARACTER VARYING(400) NOT NULL,
  cargo_nombre_alt      CHARACTER VARYING(400)
);


ALTER TABLE goc.goc_organos_reuniones_miembros OWNER TO goc;

--
-- Name: goc_p_orden_dia_acuerdos; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_p_orden_dia_acuerdos (
  id              DOUBLE PRECISION        NOT NULL,
  punto_id        DOUBLE PRECISION        NOT NULL,
  descripcion     CHARACTER VARYING(2048) NOT NULL,
  mime_type       CHARACTER VARYING(512)  NOT NULL,
  nombre_fichero  CHARACTER VARYING(512)  NOT NULL,
  fecha_adicion   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  datos           OID                   NOT NULL,
  creador_id      DOUBLE PRECISION        NOT NULL,
  descripcion_alt CHARACTER VARYING(2048)
);


ALTER TABLE goc.goc_p_orden_dia_acuerdos OWNER TO goc;

--
-- Name: goc_p_orden_dia_descriptores; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_p_orden_dia_descriptores (
  id             DOUBLE PRECISION NOT NULL,
  id_clave       DOUBLE PRECISION,
  id_p_orden_dia DOUBLE PRECISION
);


ALTER TABLE goc.goc_p_orden_dia_descriptores OWNER TO goc;

--
-- Name: goc_p_orden_dia_documentos; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_p_orden_dia_documentos (
  id              DOUBLE PRECISION        NOT NULL,
  punto_id        DOUBLE PRECISION        NOT NULL,
  descripcion     CHARACTER VARYING(2048) NOT NULL,
  mime_type       CHARACTER VARYING(512)  NOT NULL,
  nombre_fichero  CHARACTER VARYING(512)  NOT NULL,
  fecha_adicion   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  datos           OID                   NOT NULL,
  creador_id      DOUBLE PRECISION        NOT NULL,
  descripcion_alt CHARACTER VARYING(2048)
);


ALTER TABLE goc.goc_p_orden_dia_documentos OWNER TO goc;

--
-- Name: goc_reuniones; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_reuniones (
  id                          DOUBLE PRECISION       NOT NULL,
  asunto                      CHARACTER VARYING(500) NOT NULL,
  fecha                       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  duracion                    DOUBLE PRECISION,
  descripcion                 TEXT,
  creador_id                  DOUBLE PRECISION       NOT NULL,
  fecha_creacion              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  acuerdos                    TEXT,
  fecha_completada            TIMESTAMP WITHOUT TIME ZONE,
  ubicacion                   CHARACTER VARYING(4000),
  numero_sesion               DOUBLE PRECISION,
  url_grabacion               CHARACTER VARYING(4000),
  publica                     BOOLEAN,
  miembro_responsable_acta_id DOUBLE PRECISION,
  telematica                  BOOLEAN DEFAULT FALSE,
  telematica_descripcion      TEXT,
  notificada                  BOOLEAN DEFAULT FALSE,
  creador_nombre              CHARACTER VARYING(2000),
  creador_email               CHARACTER VARYING(200),
  admite_suplencia            BOOLEAN DEFAULT FALSE,
  completada                  BOOLEAN DEFAULT FALSE,
  admite_comentarios          BOOLEAN DEFAULT FALSE,
  fecha_segunda_convocatoria  TIMESTAMP WITHOUT TIME ZONE,
  asunto_alt                  CHARACTER VARYING(500),
  descripcion_alt             TEXT,
  acuerdos_alt                TEXT,
  ubicacion_alt               CHARACTER VARYING(4000),
  telematica_descripcion_alt  TEXT,
  aviso_primera_reunion       BOOLEAN DEFAULT FALSE  NOT NULL
);


ALTER TABLE goc.goc_reuniones OWNER TO goc;

--
-- Name: goc_reuniones_comentarios; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_reuniones_comentarios (
  id             DOUBLE PRECISION NOT NULL,
  comentario     TEXT             NOT NULL,
  reunion_id     DOUBLE PRECISION NOT NULL,
  creador_id     DOUBLE PRECISION NOT NULL,
  creador_nombre CHARACTER VARYING(1000),
  fecha          TIMESTAMP WITHOUT TIME ZONE NOT NULL
);


ALTER TABLE goc.goc_reuniones_comentarios OWNER TO goc;

--
-- Name: goc_reuniones_documentos; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_reuniones_documentos (
  id              DOUBLE PRECISION        NOT NULL,
  reunion_id      DOUBLE PRECISION        NOT NULL,
  descripcion     CHARACTER VARYING(2048) NOT NULL,
  mime_type       CHARACTER VARYING(512)  NOT NULL,
  nombre_fichero  CHARACTER VARYING(512)  NOT NULL,
  fecha_adicion   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  datos           OID                   NOT NULL,
  creador_id      DOUBLE PRECISION        NOT NULL,
  descripcion_alt CHARACTER VARYING(2048)
);


ALTER TABLE goc.goc_reuniones_documentos OWNER TO goc;

--
-- Name: goc_reuniones_invitados; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_reuniones_invitados (
  id             DOUBLE PRECISION        NOT NULL,
  reunion_id     DOUBLE PRECISION        NOT NULL,
  persona_id     DOUBLE PRECISION        NOT NULL,
  persona_nombre CHARACTER VARYING(1000) NOT NULL,
  persona_email  CHARACTER VARYING(1000) NOT NULL
);


ALTER TABLE goc.goc_reuniones_invitados OWNER TO goc;

--
-- Name: goc_reuniones_puntos_orden_dia; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_reuniones_puntos_orden_dia (
  id                 DOUBLE PRECISION        NOT NULL,
  titulo             CHARACTER VARYING(4000) NOT NULL,
  descripcion        TEXT                    NOT NULL,
  orden              DOUBLE PRECISION        NOT NULL,
  reunion_id         DOUBLE PRECISION        NOT NULL,
  acuerdos           TEXT,
  deliberaciones     TEXT,
  publico            BOOLEAN DEFAULT FALSE,
  titulo_alt         CHARACTER VARYING(4000),
  descripcion_alt    TEXT,
  acuerdos_alt       TEXT,
  deliberaciones_alt TEXT
);


ALTER TABLE goc.goc_reuniones_puntos_orden_dia OWNER TO goc;

--
-- Name: goc_tipos_organo; Type: TABLE; Schema: public; Owner: goc; Tablespace:
--

CREATE TABLE goc.goc_tipos_organo (
  id         DOUBLE PRECISION       NOT NULL,
  codigo     CHARACTER VARYING(10)  NOT NULL,
  nombre     CHARACTER VARYING(400) NOT NULL,
  nombre_alt CHARACTER VARYING(400)
);


ALTER TABLE goc.goc_tipos_organo OWNER TO goc;

CREATE SEQUENCE hibernate_sequence
START WITH 1
INCREMENT BY 1
  NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE goc.hibernate_sequence OWNER TO goc;

--
-- Name: miembros_pk; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_miembros
ADD CONSTRAINT miembros_pk PRIMARY KEY ( ID );

--
-- Name: organos_pk; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_organos
ADD CONSTRAINT organos_pk PRIMARY KEY ( ID );

--
-- Name: organos_reuniones_pk; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_organos_reuniones
ADD CONSTRAINT organos_reuniones_pk PRIMARY KEY ( ID );

--
-- Name: pk_clave; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_claves
ADD CONSTRAINT pk_clave PRIMARY KEY ( ID );

--
-- Name: pk_descriptor; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_descriptores
ADD CONSTRAINT pk_descriptor PRIMARY KEY ( ID );

--
-- Name: pk_ord_des; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_p_orden_dia_descriptores
ADD CONSTRAINT pk_ord_des PRIMARY KEY ( ID );

--
-- Name: reuniones_pk; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_reuniones
ADD CONSTRAINT reuniones_pk PRIMARY KEY ( ID );

--
-- Name: sys_c0087066; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_cargos
ADD CONSTRAINT sys_c0087066 PRIMARY KEY ( ID );

--
-- Name: sys_c0087076; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_reuniones_documentos
ADD CONSTRAINT sys_c0087076 PRIMARY KEY ( ID );

--
-- Name: sys_c0087083; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_reuniones_puntos_orden_dia
ADD CONSTRAINT sys_c0087083 PRIMARY KEY ( ID );

--
-- Name: sys_c0087093; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_p_orden_dia_documentos
ADD CONSTRAINT sys_c0087093 PRIMARY KEY ( ID );

--
-- Name: sys_c0087104; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_organos_reuniones_miembros
ADD CONSTRAINT sys_c0087104 PRIMARY KEY ( ID );

--
-- Name: sys_c0087110; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_reuniones_comentarios
ADD CONSTRAINT sys_c0087110 PRIMARY KEY ( ID );

--
-- Name: sys_c0087117; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_organos_autorizados
ADD CONSTRAINT sys_c0087117 PRIMARY KEY ( ID );

--
-- Name: sys_c0087142; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_descriptores_tipos_organo
ADD CONSTRAINT sys_c0087142 PRIMARY KEY ( ID );

--
-- Name: sys_c0087149; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_reuniones_invitados
ADD CONSTRAINT sys_c0087149 PRIMARY KEY ( ID );

--
-- Name: sys_c0087157; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_organos_invitados
ADD CONSTRAINT sys_c0087157 PRIMARY KEY ( ID );

--
-- Name: tipos_organo_pk; Type: CONSTRAINT; Schema: public; Owner: goc; Tablespace:
--

ALTER TABLE ONLY goc_tipos_organo
ADD CONSTRAINT tipos_organo_pk PRIMARY KEY ( ID );

--
-- Name: goc_azure_invitados_reu_i; Type: INDEX; Schema: public; Owner: goc; Tablespace:
--

CREATE INDEX goc_azure_invitados_reu_i
  ON goc_reuniones_invitados USING btree (reunion_id);

--
-- Name: uji_descriptores_tip_org_de_i; Type: INDEX; Schema: public; Owner: goc; Tablespace:
--

CREATE INDEX uji_descriptores_tip_org_de_i
  ON goc_descriptores_tipos_organo USING btree (descriptor_id);

--
-- Name: uji_descriptores_tip_org_to_i; Type: INDEX; Schema: public; Owner: goc; Tablespace:
--

CREATE INDEX uji_descriptores_tip_org_to_i
  ON goc_descriptores_tipos_organo USING btree (tipo_organo_id);

--
-- Name: fk_clave_desc; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_claves
ADD CONSTRAINT fk_clave_desc FOREIGN KEY (id_descriptor) REFERENCES goc_descriptores( ID );

--
-- Name: fk_comentarios_reuniones; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_reuniones_comentarios
ADD CONSTRAINT fk_comentarios_reuniones FOREIGN KEY (reunion_id) REFERENCES goc_reuniones( ID );

--
-- Name: fk_documentos_reuniones; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_reuniones_documentos
ADD CONSTRAINT fk_documentos_reuniones FOREIGN KEY (reunion_id) REFERENCES goc_reuniones( ID );

--
-- Name: fk_miembros_cargos; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_miembros
ADD CONSTRAINT fk_miembros_cargos FOREIGN KEY (cargo_id) REFERENCES goc_cargos( ID );

--
-- Name: fk_miembros_organos_reuniones; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_organos_reuniones_miembros
ADD CONSTRAINT fk_miembros_organos_reuniones FOREIGN KEY (organo_reunion_id) REFERENCES goc_organos_reuniones( ID );

--
-- Name: fk_ord_cla; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_p_orden_dia_descriptores
ADD CONSTRAINT fk_ord_cla FOREIGN KEY (id_clave) REFERENCES goc_claves( ID );

--
-- Name: fk_ord_ord; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_p_orden_dia_descriptores
ADD CONSTRAINT fk_ord_ord FOREIGN KEY (id_p_orden_dia) REFERENCES goc_reuniones_puntos_orden_dia( ID );

--
-- Name: fk_ordendia_acuerdos_reunion; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_p_orden_dia_acuerdos
ADD CONSTRAINT fk_ordendia_acuerdos_reunion FOREIGN KEY (punto_id) REFERENCES goc_reuniones_puntos_orden_dia( ID );

--
-- Name: fk_ordendia_documentos_reunion; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_p_orden_dia_documentos
ADD CONSTRAINT fk_ordendia_documentos_reunion FOREIGN KEY (punto_id) REFERENCES goc_reuniones_puntos_orden_dia( ID );

--
-- Name: fk_ordendia_reunion; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_reuniones_puntos_orden_dia
ADD CONSTRAINT fk_ordendia_reunion FOREIGN KEY (reunion_id) REFERENCES goc_reuniones( ID );

--
-- Name: goc_descriptores_tipos_org_de; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_descriptores_tipos_organo
ADD CONSTRAINT goc_descriptores_tipos_org_de FOREIGN KEY (descriptor_id) REFERENCES goc_descriptores( ID );

--
-- Name: goc_descriptores_tipos_org_to; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_descriptores_tipos_organo
ADD CONSTRAINT goc_descriptores_tipos_org_to FOREIGN KEY (tipo_organo_id) REFERENCES goc_tipos_organo( ID );

--
-- Name: goc_miembros_organos_fk; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_miembros
ADD CONSTRAINT goc_miembros_organos_fk FOREIGN KEY (organo_id) REFERENCES goc_organos( ID );

--
-- Name: goc_org_reu_reuniones_fk; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_organos_reuniones
ADD CONSTRAINT goc_org_reu_reuniones_fk FOREIGN KEY (reunion_id) REFERENCES goc_reuniones( ID );

--
-- Name: goc_org_tipos_fk; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_organos
ADD CONSTRAINT goc_org_tipos_fk FOREIGN KEY (tipo_organo_id) REFERENCES goc_tipos_organo( ID );

--
-- Name: goc_reuniones_invitados_r01; Type: FK CONSTRAINT; Schema: public; Owner: goc
--

ALTER TABLE ONLY goc_reuniones_invitados
ADD CONSTRAINT goc_reuniones_invitados_r01 FOREIGN KEY (reunion_id) REFERENCES goc_reuniones( ID );

--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA
  goc TO PUBLIC;

--
-- PostgreSQL database dump complete
--

