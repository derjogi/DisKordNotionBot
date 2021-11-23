rootProject.name = "DisKordNotionBot"

// Checkout https://github.com/derjogi/notion-sdk-kotlin.git into a parallel folder for this line to work:
includeBuild("../notion-sdk-kotlin")
// (... if you want to improve that code to make it work with relations...)
//project(":notion-sdk-kotlin").projectDir = File("../notion-sdk-kotlin")