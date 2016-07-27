# OBP-JVM
The Open Bank Project REST Specification uses the [OpenAPI Specification](
https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md).

Dependencies

  mvn dependency:tree -DoutputFile=mvn.log
  mvn dependency:go-offline               copies all dependencies
  mvn versions:display-dependency-updates -N available updates for dependencies
  mvn versions:display-plugin-updates     -N available updates for plugins
  mvn versions:display-property-updates   -N

Settings

  local.xml creates a maven repository in the current folder
  offline.xml uses the local repository only



Git

  Einem neuen, fernen Zweig lokal folgen:

  git remote show origin

  If the remote branch you want to checkout is under "New remote branches" and
  not "Tracked remote branches" then you need to fetch them first:

  git remote update
  git fetch

  Now it should work:

  git checkout -b local-name origin/remote-name

  Veränderte Dateien anzeigen

  git show --stat <commitish>
  git show --stat HEAD^^..HEAD
  git show --stat --source HEAD^^..HEAD
  git diff-tree --name-only -r HEAD^^..HEAD
  git log --stat HEAD^..HEAD




