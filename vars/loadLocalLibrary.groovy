#!groovy
import hudson.plugins.git.GitSCM

def call(GitSCM scm, String libraryPath, String masterNode = 'master') {
	node(masterNode) {
		echo "Loading local shared library"
		checkout scm

		// Create new git repo inside jenkins subdirectory
		sh("""cd ./$libraryPath && \
				(rm -rf .git || true) && \
				git init && \
				git add --all && \
				git commit -m init
		""")
		def repoPath = sh(returnStdout: true, script: 'pwd').trim() + "/$libraryPath"

		library identifier: 'local-lib@master', 
				retriever: modernSCM([$class: 'GitSCMSource', remote: "$repoPath"]), 
				changelog: false

		deleteDir() // After loading the library we can clean the workspace
		echo "Done loading shared library"
	}
}
