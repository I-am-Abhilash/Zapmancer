package com.smach.zapmancer.core.common

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import java.util.UUID.randomUUID
import kotlin.math.ceil

@Suppress("MagicNumber")
object Sanitizer {
    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()

    private val safelist = Safelist.relaxed()
        .addTags("h1", "h2", "h3", "h4", "h5", "h6", "code", "pre", "span", "div", "hr", "br")
        .addAttributes("code", "class")
        .addAttributes("pre", "class")
        .addAttributes("span", "class", "style")
        .addAttributes("div", "class", "style")
        .addProtocols("a", "href", "http", "https", "mailto")
        .addProtocols("img", "src", "http", "https")

    fun processMarkdown(markdown: String): String {
        val html = renderer.render(parser.parse(markdown))
        return Jsoup.clean(html, safelist)
    }

    fun sanitizeHtml(html: String): String = Jsoup.clean(html, safelist)

    fun getTextOnly(html: String): String = Jsoup.parse(html).text()

    fun getSnippet(html: String, maxLength: Int = 200): String {
        val text = getTextOnly(html)
        if (text.length <= maxLength) return text
        return text.take(maxLength).substringBeforeLast(" ") + "..."
    }

    fun calculateReadingTime(html: String, wordsPerMinute: Int = 200): Int {
        val text = getTextOnly(html)
        val wordCount = text.split(Regex("\\s+")).filter { it.isNotEmpty() }.size
        return ceil(wordCount.toDouble() / wordsPerMinute).toInt().coerceAtLeast(1)
    }

    fun slugify(input: String): String = input.lowercase()
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace(Regex("\\s+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')

    fun generateUniqueSlug(title: String): String = "${slugify(title)}-${randomUUID().toString().take(8)}"
}
