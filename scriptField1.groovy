import com.atlassian.jira.component.ComponentAccessor
import java.sql.Date
import java.sql.Timestamp
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import org.apache.log4j.Logger
import org.apache.log4j.Level
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter

import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.issue.search.SearchException
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder

import com.atlassian.jira.issue.fields.CustomField
import groovy.transform.Field
import java.text.SimpleDateFormat 

// Manager
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()

// Some components
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def searchService = ComponentAccessor.getComponentOfType(SearchService)
def issueManager = ComponentAccessor.getIssueManager()

// change to 'true' if you want to send an email if the update is successful
final boolean sendMail = false

def issueService = ComponentAccessor.issueService


// 获取bigpicture的program Id
final String programIdName = "BPProgram"

// Get the custom field objectså
CustomField programIdCustomField = customFieldManager.getCustomFieldObjects(issue).find { it.name == programIdName }

log.warn("programIdCustomField is  ${programIdCustomField}")

String programId = issue?.getCustomFieldValue(programIdCustomField)

// 查询下级不是Done的issue
def query = "issue in allBoxParentTasks(${programId}, ${issue.key})"

// Parse the query
def parseResult = searchService.parseQuery(user, query)
if (!parseResult.valid) {
    log.error('Invalid query ${query}')
    return null
}

try {
    // Perform the query to get the issues
    def results = searchService.search(user, parseResult.query, PagerFilter.unlimitedFilter)
    def issues = results.results
    log.warn("Total issues: ${issues.size()}")
    Double dsum = new Double(issues.size()+1)
    return dsum
} catch (SearchException e) {
    e.printStackTrace()
    null
}
