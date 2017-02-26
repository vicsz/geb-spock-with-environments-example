import org.openqa.selenium.Platform
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

//Output directory for Geb -- will include screenshots/html at the end of test
reportsDir = new File("build/geb-reports")

//Only do Geb reports on broken tests
reportOnTestFailureOnly = true

if(!System.properties.'geb.env' && !System.properties.'phantomjs.binary.path')
    installPhantomJs('2.1.1')

if(System.properties.'geb.env' == 'chrome' && !System.properties.'webdriver.chrome.driver')
    installChrome('2.24')


//Default WebDriver (i.e. when running out of Intellij)
driver = { new PhantomJSDriver() }

environments {

    chrome {
        driver = { new ChromeDriver() }
    }

}

void installPhantomJs(String phantomJSVersion ){
    String platform
    String archiveExtension
    String execFilePath

    if (Platform.current.is(Platform.WINDOWS)) {
        execFilePath = 'phantomjs.exe'
        platform = 'windows'
        archiveExtension = 'zip'
    }
    else if (Platform.current.is(Platform.MAC)) {
        execFilePath = '/bin/phantomjs'
        platform = 'macosx'
        archiveExtension = 'zip'
    } else if (Platform.current.is(Platform.LINUX)) {
        execFilePath = '/bin/phantomjs'
        platform = 'linux-i686'
        archiveExtension = 'tar.bz2'
    } else {
        throw new RuntimeException("Unsupported operating system [${Platform.current}]")
    }

    String phantomjsExecPath = "phantomjs-${phantomJSVersion}-${platform}/${execFilePath}"

    String phantomJsFullDownloadPath = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-${phantomJSVersion}-${platform}.${archiveExtension}"

    File phantomJSDriverLocalFile = downloadDriver(phantomJsFullDownloadPath, phantomjsExecPath, archiveExtension, '')

    System.setProperty('phantomjs.binary.path', phantomJSDriverLocalFile.absolutePath)
}

void installChrome(String chromeVersion){
    String platform
    String execFilePath

    if (Platform.current.is(Platform.WINDOWS)) {
        execFilePath = 'chromedriver.exe'
        platform = 'win32'
    }
    else if (Platform.current.is(Platform.MAC)) {
        execFilePath = 'chromedriver'
        platform = 'mac64'
    } else if (Platform.current.is(Platform.LINUX)) {
        execFilePath = 'chromedriver'
        platform = 'amd64'
    } else {
        throw new RuntimeException("Unsupported operating system [${Platform.current}]")
    }

    String chromeExecPath = "chromedriver-${chromeVersion}-${platform}/${execFilePath}"

    String chromeFullDownloadPath = "http://chromedriver.storage.googleapis.com/${chromeVersion}/chromedriver_${platform}.zip"

    String unzipPath = "/chromedriver-${chromeVersion}-${platform}"

    File chromeDriverLocalFile = downloadDriver(chromeFullDownloadPath, chromeExecPath, 'zip', unzipPath)

    System.properties.'webdriver.chrome.driver' = chromeDriverLocalFile.absolutePath
}

private File downloadDriver(String driverDownloadFullPath, String driverFilePath, String archiveFileExtension, String unzipPath) {
    File destinationDirectory = new File("target/drivers"+unzipPath)
    if (!destinationDirectory.exists()) {
        destinationDirectory.mkdirs()
    }

    File driverFile = new File("target/drivers/${driverFilePath}")

    String localArchivePath = "target/driver.${archiveFileExtension}"

    if (!driverFile.exists()) {
        def ant = new AntBuilder()
        ant.get(src: driverDownloadFullPath, dest: localArchivePath)

        if (archiveFileExtension == "zip") {
            ant.unzip(src: localArchivePath, dest: destinationDirectory)
        } else {
            ant.untar(src: localArchivePath, dest: destinationDirectory, compression: 'bzip2')
        }

        ant.delete(file: localArchivePath)
        ant.chmod(file: driverFile, perm: '700')
    }

    return driverFile
}