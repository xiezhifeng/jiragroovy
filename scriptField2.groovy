import com.atlassian.jira.component.ComponentAccessor
import java.sql.Date
import java.sql.Timestamp
import java.lang.Math
import java.util.concurrent.TimeUnit
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import org.apache.log4j.Logger
import org.apache.log4j.Level

// Manager
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()

// 开始日期
final String startDate = "Start date"
// 结束日期
final String endDate = "End date"

// 基线开始日期
final String baselineStartDate = "Baseline start date"
// 基线结束日期
final String baselineEndDate = "Baseline end date"

// Get the custom field objects
CustomField startDateCustomField = customFieldManager.getCustomFieldObjects(issue).find { it.name == startDate }
CustomField endDateCustomField = customFieldManager.getCustomFieldObjects(issue).find { it.name == endDate }
CustomField baselineStartDateCustomField = customFieldManager.getCustomFieldObjects(issue).find { it.name == baselineStartDate }
CustomField baselineEndDateCustomField = customFieldManager.getCustomFieldObjects(issue).find { it.name == baselineEndDate }


Timestamp endDateValue  = null
if(issue?.getCustomFieldValue(endDateCustomField))
{ 
    endDateValue = (Timestamp)issue.getCustomFieldValue(endDateCustomField)
}

Timestamp baselineEndDateValue  = null
if(issue?.getCustomFieldValue(baselineEndDateCustomField))
{ 
    baselineEndDateValue = (Timestamp)issue.getCustomFieldValue(baselineEndDateCustomField)
}

long totaldDelayDaysValue = 0

if(baselineEndDateValue){ 
    long diffInMillies = Math.abs(endDateValue.getTime() - baselineEndDateValue.getTime())
    totaldDelayDaysValue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
}

Double d= new Double(totaldDelayDaysValue)

return d
