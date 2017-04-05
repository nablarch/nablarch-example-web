SELECT
    client_id,
    client_name,
    client.industry_code,
    industry_name
FROM
    client
INNER JOIN
    industry ON client.industry_code = industry.industry_code
WHERE
    /*%if @isNotEmpty(dto.clientName) */
    client_name LIKE /* @infix(dto.clientName) */'%株式会社%'
    /*%end*/
    /*%if @isNotEmpty(dto.industryCode) */
    AND client.industry_code = /* dto.industryCode */'01'
    /*%end*/
ORDER BY
    /*%if dto.sortId == "clientIdAsc" */
    client_id
    /*%elseif dto.sortId == "clientIdDesc" */
    client_id DESC
    /*%elseif dto.sortId == "clientNameAsc" */
    client_name, client_id
    /*%elseif dto.sortId == "clientNameDesc" */
    client_name DESC , client_id DESC
    /*%end*/
