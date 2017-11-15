/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.integtests.fixtures

import org.gradle.integtests.fixtures.publish.RemoteRepositorySpec
import org.gradle.integtests.fixtures.resolve.ResolveTestFixture
import org.junit.runner.RunWith

@RunWith(GradleMetadataResolveRunner)
abstract class AbstractModuleDependencyResolveTest extends AbstractHttpDependencyResolutionTest {
    final ResolveTestFixture resolve = new ResolveTestFixture(buildFile, "conf")

    private final RemoteRepositorySpec repoSpec = new RemoteRepositorySpec()

    boolean useIvy() {
        GradleMetadataResolveRunner.useIvy()
    }

    String getRootProjectName() { 'test' }

    private String getMavenRepository() {
        """
            repositories {
                maven { 
                   url "${mavenHttpRepo.uri}"
                   ${GradleMetadataResolveRunner.isGradleMetadataEnabled() ? 'useGradleMetadata()' : ''}
                }
            }
        """
    }

    private String getIvyRepository() {
        """
            repositories {
                ivy { 
                   url "${ivyHttpRepo.uri}"
                   ${GradleMetadataResolveRunner.isGradleMetadataEnabled() ? 'useGradleMetadata()' : ''}
                }
            }
        """
    }

    def getRepository() {
        useIvy() ? ivyRepository : mavenRepository
    }

    def setup() {
        settingsFile << "rootProject.name = '$rootProjectName'"
        resolve.prepare()
        server.start()
        buildFile << """
            $repository

            configurations {
                conf
            }
        """
    }

    def cleanup() {
        server.stop()
    }

    void repository(@DelegatesTo(RemoteRepositorySpec) Closure<Void> spec) {
        spec.delegate = repoSpec
        spec()
    }

    void repositoryInteractions(@DelegatesTo(RemoteRepositorySpec) Closure<Void> spec) {
        spec.delegate = repoSpec
        spec()
        repoSpec.build(useIvy() ? ivyHttpRepo : mavenHttpRepo)
    }

}
