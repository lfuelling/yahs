#!/bin/bash

if [[ "$TRAVIS_REPO_SLUG" == "lfuelling/yahs" ]]; then
  echo -e "Deploying package..."
  mvn deploy -DaltDeploymentRepository=https://maven.pkg.github.com/lfuelling -Dtoken="${GH_TOKEN}"
  echo -e "Deployed package."
fi
