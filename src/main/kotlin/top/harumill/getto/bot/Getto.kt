package top.harumill.getto.bot

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.util.zip.GZIPInputStream
import javax.imageio.ImageIO

object Getto {

    /**
     * 下载文件
     */
    fun downloadFile(url: String, pathName: String) {
        val openConnection = URL(url).openConnection()
        val contentType = openConnection.contentType
        var type = "."
        var copyOrNot = false
        contentType.forEach {
            if (it == '/' && !copyOrNot)
                copyOrNot = true
            else if (copyOrNot) {
                type = type.plus(it)
            }
        }
//        println(copyOrNot)
        val file = File(pathName + type)
        //防止某些网站跳转到验证界面
//        openConnection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        //如果图片是采用gzip压缩
        val bytes = if (openConnection.contentEncoding == "gzip") {
            GZIPInputStream(openConnection.getInputStream()).readBytes()
        } else {
            openConnection.getInputStream().readBytes()
        }
        file.writeBytes(bytes)
//        return file
    }

    fun getDownloadFile(url: String, pathName: String): File {
        val openConnection = URL(url).openConnection()
        val contentType = openConnection.contentType
        var type = "."
        var copyOrNot = false
        contentType.forEach {
            if (it == '/' && !copyOrNot)
                copyOrNot = true
            else if (copyOrNot) {
                type = type.plus(it)
            }
        }
        val file = File(pathName + type)
        //防止某些网站跳转到验证界面
//        openConnection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        //如果图片是采用gzip压缩
        val bytes = if (openConnection.contentEncoding == "gzip") {
            GZIPInputStream(openConnection.getInputStream()).readBytes()
        } else {
            openConnection.getInputStream().readBytes()
        }
        file.writeBytes(bytes)
        return file
    }

    /**
     *  获取对应img目录下所有图片
     */
    fun getImgList(path: String): MutableList<String> {
        val files: MutableList<String> = mutableListOf()
        val fileTree: FileTreeWalk = File(path).walk()
        fileTree.maxDepth(1)
            .filter { it.isFile }
            .forEach { files.add(it.name) }
        return files
    }

    fun addTextToImage(imgFile: File, text: String, imgInfo: ImgInfo, outFile: File) {
        val img: BufferedImage = ImageIO.read(imgFile)

        val imageGraphics: Graphics2D = img.createGraphics()
        // 设置高清字体
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        imageGraphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT
        )
        imageGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        // 设置文字 color
        imageGraphics.color = imgInfo.color
        // 设置文体 style
        imageGraphics.font = Font("微软雅黑", Font.PLAIN, imgInfo.size)
        // 标准化必须纠正的量
        val fixedOffsetY = imageGraphics.fontMetrics.ascent - (imageGraphics.fontMetrics.height / 2 - imgInfo.size / 2)
        // 文字上图
        imageGraphics.drawString(text, imgInfo.x, imgInfo.y + fixedOffsetY)

        ImageIO.write(img, imgFile.extension, outFile)
    }

    fun calculate(input: String): String {
        return Calculator.getResult(input)
    }
}