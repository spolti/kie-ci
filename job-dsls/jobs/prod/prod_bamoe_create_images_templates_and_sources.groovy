/**
* Create the OpenShift images templates and sources
*/
def scriptTemplate = this.getClass().getResource("job-scripts/prod_bamoe_create_images_templates_and_sources.jenkinsfile").text
def parsedScript = scriptTemplate.replaceAll(/<%=\s*(\w+)\s*%>/) { config[it[1]] ?: '' }

def folderPath = "PROD"
folder(folderPath)

pipelineJob("${folderPath}/bamoe-create-images-templates-and-sources") {
    description('This job creates the Openshift images templates and sources for BAMOE.')

    parameters {
        stringParam('VERSION', '', ' The milestone version, i.e. 8.0.3')
        stringParam("PRODUCT_MILESTONE", "CR1")
        stringParam('BUILDS', '', 'List of Brew builds separated by comma')
        stringParam('OVERRIDING_FILES', 'branch-overrides.yaml', 'Comma separated list of the overriding files that will be fetched from the images repositories')
        stringParam('GITHUB_REFERENCE', '', 'Override the GitHub reference for all cloned repositories')
        booleanParam('UPLOAD_ARTIFACTS', true, 'If the generated artifacts should be uploaded to rcm-host')
    }

    logRotator {
        numToKeep(20)
    }

    definition {
        cps {
            script(parsedScript)
            sandbox()
        }
    }

}