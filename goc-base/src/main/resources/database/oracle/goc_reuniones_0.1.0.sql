alter table goc_reuniones modify (duracion null);

alter table goc_reuniones add (fecha_segunda_convocatoria date null);

alter table goc_cargos add (nombre_alt varchar2(400));
alter table goc_organos add (nombre_alt varchar2(400));
alter table goc_organos_reuniones modify (organo_nombre varchar2(400));
alter table goc_organos_reuniones add (organo_nombre_alt varchar2(400));
alter table goc_organos_reuniones_miembros add (cargo_nombre_alt varchar2(400));
alter table goc_p_orden_dia_documentos add (descripcion_alt varchar2(2048));
alter table goc_reuniones add (asunto_alt varchar2(500));
alter table goc_reuniones add (descripcion_alt clob);
alter table goc_reuniones add (acuerdos_alt clob);
alter table goc_reuniones add (ubicacion_alt varchar2(4000));
alter table goc_reuniones add (telematica_descripcion_alt clob);
alter table goc_reuniones_documentos add (descripcion_alt varchar2(2048));
alter table goc_reuniones_puntos_orden_dia add (titulo_alt varchar2(4000));
alter table goc_reuniones_puntos_orden_dia add (descripcion_alt clob);
alter table goc_reuniones_puntos_orden_dia add (acuerdos_alt clob);
alter table goc_reuniones_puntos_orden_dia add (deliberaciones_alt clob);
alter table goc_tipos_organo modify (nombre varchar2(400));
alter table goc_tipos_organo add (nombre_alt varchar2(400));