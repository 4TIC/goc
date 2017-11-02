create table goc_cargos (
  id      number not null primary key,
  nombre  varchar2(400)
);

alter table GOC_MIEMBROS
  add cargo_id number;

alter table GOC_MIEMBROS
  add constraint fk_miembros_cargos foreign key (cargo_id) references goc_cargos(id);

alter table GOC_REUNIONES add ACTA CLOB;
alter table GOC_REUNIONES add FECHA_COMPLETADA DATE;