Push-Location $PSScriptRoot
$archive=".\target\lambda-deployment.zip"
if (test-path $archive){
    Remove-Item -Path $archive
}
7z a $archive ..\..\venv\Lib\site-packages\*
7z a $archive .\*.py
7z a $archive .\*.properties
Pop-Location



