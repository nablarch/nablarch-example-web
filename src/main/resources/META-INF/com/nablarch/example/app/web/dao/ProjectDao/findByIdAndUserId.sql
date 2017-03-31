select
  project.project_id,
  project.project_name,
  project.project_type,
  project.project_class,
  project.project_start_date,
  project.project_end_date,
  project.client_id,
  project.project_manager,
  project.project_leader,
  project.user_id,
  project.note,
  project.sales,
  project.cost_of_goods_sold,
  project.sga,
  project.allocation_of_corp_expenses,
  project.version,
  client.client_name
from
  project
  inner join client
    on project.client_id = client.client_id
where
  project.project_id = /*id*/100
  and project.user_id = /*userId*/200
