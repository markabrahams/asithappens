Set wshShell = CreateObject("Wscript.Shell")


' Section 1: Location variables - change to suit your environment
javaExe = "C:\Program Files\Java\jre1.5.0_06\bin\java"
asithappensHome = "C:\Program Files\asithappens"


' Section 2: System variables - do not change
asithappensLib = asithappensHome & "\lib"
asithappensJar = asithappensHome & "\dist\asithappens.jar"
asithappensNative = asithappensHome & "\lib\native"


' Section 3: Start-up code

' Build Java CLASSPATH
classpath = asithappensJar
Set fso = CreateObject("Scripting.FileSystemObject")
Set libFolder = fso.GetFolder(asithappensLib)
Set libCollection = libFolder.Files
For Each fileName in libCollection
    classpath = classpath & ";" & asithappensLib & "\" & fileName.name
Next

' Build command
commandExe = """" & javaExe & """ -cp """ & classpath & """ -Djava.library.path=""" & asithappensNative & """ nz.co.abrahams.asithappens.mainui.AsItHappens"

' Run command
wshShell.CurrentDirectory = asithappensHome
wshShell.run commandExe, 0, False
