select
  project_id,
  project_name,
  project_type,
  project_class,
  project_start_date,
  project_end_date,
  client_id,
  project_manager,
  project_leader,
  user_id,
  note,
  version
from
  project
where
  user_id = /* condition.userId */10
  /*%if condition.clientId != null */
  and client_id = /*condition.clientId*/10
  /*%end*/
  /*%if @isNotEmpty(condition.projectName) */
  and project_name like /*@infix(condition.projectName)*/'%プロジェクト%'
  /*%end*/
  /*%if @isNotEmpty(condition.projectType) */
  and project_type = /*condition.projectType*/'A'
  /*%end*/
  /*%if condition.getProjectClassList() != null  */
  and project_class in /*condition.getProjectClassList()*/('A', 'B')
  /*%end*/
  /*%if condition.projectStartDateBegin != null */
  and project_start_date >= /*condition.projectStartDateBegin*/'2017-01-01'
  /*%end*/
  /*%if condition.projectStartDateEnd != null */
  and project_start_date <= /*condition.projectStartDateEnd*/'2017-12-31'
  /*%end*/
  /*%if condition.projectEndDateBegin != null */
  and project_end_date >= /*condition.projectEndDateBegin*/'2017-01-01'
  /*%end*/
  /*%if condition.projectEndDateEnd != null*/
  and project_end_date <= /*condition.projectEndDateEnd */'2017-12-31'
/*%end*/
order by
  /*%if condition.sortId == "idAsc" */
  project_id
  /*%elseif condition.sortId == "idDesc" */
  project_id desc
  /*%elseif condition.sortId == "nameAsc"*/
  project_name, project_id
  /*%elseif condition.sortId == "nameDesc"*/
  project_name desc, project_id desc
  /*%elseif condition.sortId == "startDateAsc"*/
  project_start_date, project_id
  /*%elseif condition.sortId == "startDateDesc"*/
  project_start_date desc, project_id desc
  /*%elseif condition.sortId == "endDateAsc"*/
  project_end_date, project_id
  /*%elseif condition.sortId == "endDateDesc"*/
  project_end_date desc, project_id desc
  /*%else*/
  project_id
  /*%end*/


