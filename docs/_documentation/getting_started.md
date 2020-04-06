---
title: Getting Started
position: 1
parameters:
  - name:
    content:
content_markdown: |-
  Welcome to Galileo.

  Android library that helps you to debug applications directly from your android device. Galileo works based on plugins and it is easy to be extensibled, we are open for new plugins!

  This API is still under development and will evolve.

  Versions are deployed to jCenter, you need this in your gradle files:

  - code_block: |-
        repositories {
                jcenter()
            }
      title: jCenter
      language: groovy

  - code_block: |-
          debugImplementation "com.josedlpozo.galileo:galileo:$GALILEO_VERSION"
              releaseImplementation "com.josedlpozo.galileo:galileo-no-op:$GALILEO_VERSION"
        title: jCenter
        language: groovy

  
left_code_blocks:
  - code_block:
    title:
    language:
right_code_blocks:
  - code_block:
    title:
    language:
---