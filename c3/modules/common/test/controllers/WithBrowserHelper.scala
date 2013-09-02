package controllers

import play.api.test.WithBrowser
import org.fluentlenium.core.filter.Filter
import org.fluentlenium.core.Fluent

trait WithBrowserHelper {
  this: WithBrowser[_] =>

  def fluent = browser clear "#nothing"

  def goTo(relativeURL: String) = browser.goTo(relativeURL)

  def next = browser submit "button[value='next']"

  def back = browser click "#backButton"

  def click(cssSelector: String, filters: Filter*) = browser click(cssSelector, filters: _*)

  def fill() = this

  def in(f: => Fluent) = f

  def fill(cssSelector: String, filters: Filter*) = browser fill(cssSelector, filters: _*)

  def $(name: String, filters: Filter*) = browser $(name, filters: _*)

  def findFirst(name: String, filters: Filter*) = browser findFirst(name, filters: _*)

  def findAll(name: String, filters: Filter*) = browser find(name, filters: _*)
}