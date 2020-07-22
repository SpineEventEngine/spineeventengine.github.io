This repository contains the code of [Spine Event Engine framework site](https://spine.io).

The website uses [Jekyll](https://jekyllrb.com/) templates and is 
hosted on [GitHub Pages](https://pages.github.com/). 

## Running the Site on the Local Server

To build and launch the site on the local server:
```
bundle exec jekyll serve
```
You may be required to install Gems first:
```
bundle install
```
If you experience issues with this step, please check out the [troubleshooting guide](TROUBLESHOOTING.md).

## Using URLs in Markdown

### Links to Blog Posts

There are thee rules to follow:

#### Rule 1 -- Always concatenate Jekyll and Liquid tags

| Good                                      | Bad                                        |
|-------------------------------------------|--------------------------------------------|
| `href="{{ site.baseurl }}{{ post.url }}"` | `href="{{ site.baseurl }}/{{ post.url }}"` |

This removes the double-slash from your site's URLs.

#### Rule 2 -- *(Almost)* Always start links with `{{ site.baseurl }}`

| Good                                      | Bad                     |
|-------------------------------------------|-------------------------|
| `href="{{ site.baseurl }}{{ post.url }}"` | `href="{{ post.url }}"` |

This fixes almost all of the in-site links. The next rule covers the remainder.

**Exception**: Start hyperlinks with `{{ site.url }}{{ site.baseurl }}` in feed pages, like `atom.xml`.

#### Rule 3 -- Always use a trailing slash after `{{ site.baseurl }}`

| Good                                      | Bad                                      |
|-------------------------------------------|------------------------------------------|
| `href="{{ site.baseurl }}/" title="Home"` | `href="{{ site.baseurl }}" title="Home"` |

| Good                                           | Bad                                           |
|------------------------------------------------|-----------------------------------------------|
| `href="{{ site.baseurl }}/public/favicon.ico"` | `href="{{ site.baseurl }}public/favicon.ico"` |


Visit [Configuring Jekyll for Project GitHub Pages and for User GitHub Pages](http://downtothewire.io/2015/08/15/configuring-jekyll-for-user-and-project-github-pages/) if you want to know why these rules should be followed.

## Adding collapsible list for sidebar navigation

For collapsible categories we use [Bootstrap](https://getbootstrap.com/docs/3.3/javascript/#collapse) JS component.

To add a collapsible category use the following code:
```
<h6 class="doc-side-nav-title no-anchor collapsed" data-toggle="collapse" data-target="#collapseCategoryName" aria-expanded="false" aria-controls="collapseCategoryName">CategoryName</h6>
<ul class="collapse" id="collapseConcepts">
    <li class="doc-side-nav-list-item"><a href="{{ site.baseurl }}/docs/guides/concepts/some-link-1.html" {% if current[3] == 'some-link-1.html' %}class='current'{% endif %}>Some link name 1</a></li>
    <li class="doc-side-nav-list-item"><a href="{{ site.baseurl }}/docs/guides/concepts/some-link-2.html" {% if current[3] == 'some-link-2.html' %}class='current'{% endif %}>Some link name 2</a></li>
    <li class="doc-side-nav-list-item"><a href="{{ site.baseurl }}/docs/guides/concepts/some-link-3.html" {% if current[3] == 'some-link-3.html' %}class='current'{% endif %}>Some link name 3</a></li>
    <li class="doc-side-nav-list-item"><a href="{{ site.baseurl }}/docs/guides/concepts/some-link-4.html" {% if current[3] == 'some-link-4.html' %}class='current'{% endif %}>Some link name 4</a></li>
    <li class="doc-side-nav-list-item"><a href="{{ site.baseurl }}/docs/guides/concepts/some-link-5.html" {% if current[3] == 'some-link-5.html' %}class='current'{% endif %}>Some link name 5</a></li>
</ul>
```

### Adding code examples

We use the [code-excerpter](https://github.com/chalin/code_excerpter) tool for adding the source
code to Markdown pages. See [this doc](_samples/README.md) for the instructions.

# Testing broken links

We use the [html-proofer](https://github.com/gjtorikian/html-proofer) tool to test broken links.
To start test locally you may be required to install the Gem of the tool first:

```bash
bundle install
```
... and then build the site:
 
```bash
jekyll build
``` 

After that, please use the following command:

```bash
htmlproofer --assume-extension ./_site --only_4xx --http-status-ignore "429"
```

> Please note that links to GitHub are ignored by `--http-status-ignore "429"` command, because GitHub rejects the check 
> coming from `htmlproofer`. Details of this are described in
> [this issue](https://github.com/gjtorikian/html-proofer/issues/226).
> We only log errors for links that fall within the 4xx status code range. Redirects and server errors are not reported.  

Also, we have a GitHub Action which tests the links when the pull request is created to the `master`. 
Please see the [`.github/workflows/proof-links.yml`](.github/workflows/proof-links.yml) file for details.
