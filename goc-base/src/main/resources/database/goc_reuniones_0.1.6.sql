alter table goc_organos_reuniones_miembros add (
    delegado_voto_id number,
    delegado_voto_nombre varchar2(1000),
    delegado_voto_email varchar2(200)
)

alter table goc_reuniones add (admite_delegacion_voto number(1) default 0 not null)