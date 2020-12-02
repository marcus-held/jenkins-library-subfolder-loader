#!groovy
import hudson.plugins.git.GitSCM

def call(String libPath, String masterNode = 'master') {
	node(masterNode) {
		echo("Updating shared library from: ${libPath}");

        def pwd = sh(returnStdout: true, script: 'pwd').trim();
		library([
            identifier: 'local-lib@master', 
            retriever: modernSCM([
                $class: 'GitSCMSource',
                remote: "${pwd}/${libPath}",
            ]), 
            changelog: false,
        ]);

		echo("Done Updating shared library");
	}
}
