CREATE TABLE GOC_DESCRIPTORES (
  ID NUMBER,
  DESCRIPTOR VARCHAR(100),
  DESCRIPTOR_ALT VARCHAR(100),
  DESCRIPCION VARCHAR(200),
  DESCRIPCION_ALT VARCHAR(200),
  ESTADO NUMBER(1),
  CONSTRAINT PK_DESCRIPTOR PRIMARY KEY (ID)
);

CREATE TABLE GOC_CLAVES (
  ID NUMBER,
  CLAVE VARCHAR(100),
  CLAVE_ALT VARCHAR(100),
  ESTADO NUMBER(1),
  ID_DESCRIPTOR NUMBER,
  CONSTRAINT PK_CLAVE PRIMARY KEY (ID),
  CONSTRAINT FK_CLAVE_DESC FOREIGN KEY (ID_DESCRIPTOR) REFERENCES GOC_DESCRIPTORES (ID)
);

CREATE TABLE GOC_P_ORDEN_DIA_DESCRIPTORES (
  ID NUMBER,
  ID_CLAVE NUMBER,
  ID_P_ORDEN_DIA NUMBER,
  CONSTRAINT PK_ORD_DES PRIMARY KEY (ID),
  CONSTRAINT FK_ORD_CLA FOREIGN KEY (ID_CLAVE) REFERENCES GOC_CLAVES (ID),
  CONSTRAINT FK_ORD_ORD FOREIGN KEY (ID_P_ORDEN_DIA) REFERENCES GOC_REUNIONES_PUNTOS_ORDEN_DIA (ID)
)