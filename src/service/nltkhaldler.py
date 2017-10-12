import nltk

from src.service import bigram_tagger, cfg


class NPExtractor(object):
    def __init__(self, sentence):
        self.sentence = sentence

    # Split the sentence into singlw words/tokens
    def tokenize_sentence(self, sentence):
        tokens = nltk.word_tokenize(sentence)
        return tokens

    # Normalize brown corpus' tags ("NN", "NN-PL", "NNS" > "NN")
    def normalize_tags(self, tagged):
        n_tagged = []
        for t in tagged:
            if t[1] == "NP-TL" or t[1] == "NP":
                n_tagged.append((t[0], "NNP"))
                continue
            if t[1].endswith("-TL"):
                n_tagged.append((t[0], t[1][:-3]))
                continue
            if t[1].endswith("S"):
                n_tagged.append((t[0], t[1][:-1]))
                continue
            n_tagged.append((t[0], t[1]))
        return n_tagged

    # Extract the main topics from the sentence
    def extract(self):

        tokens = self.tokenize_sentence(self.sentence)
        tags = self.normalize_tags(bigram_tagger.tag(tokens))

        merge = True
        while merge:
            merge = False
            for x in range(0, len(tags) - 1):
                t1 = tags[x]
                t2 = tags[x + 1]
                key = "%s+%s" % (t1[1], t2[1])
                value = cfg.get(key, '')
                if value:
                    merge = True
                    tags.pop(x)
                    tags.pop(x)
                    match = "%s %s" % (t1[0], t2[0])
                    pos = value
                    tags.insert(x, (match, pos))
                    break

        matches = []
        for t in tags:
            if t[1] == "NNP" or t[1] == "NNI":
                # if t[1] == "NNP" or t[1] == "NNI" or t[1] == "NN":
                matches.append(t[0])
        return matches

    @staticmethod
    def extractKeywords(sentence: str, top=3, filterwords=None)->set:
        """
        提取一段不包含中文字符串的关键词，返回前 top 个词
        filterwords 为过滤词，通过 nltk 提取的关键词中如果
        含有过滤词，则不返回这个词
        :type top: int
        """
        np_extractor = NPExtractor(sentence)
        result = np_extractor.extract()  # result 为 list集合
        keywords = set()
        number = 0

        for word in result:
            if number > top:
                break
            if filterwords:
                isAdd = True
                for filterword in filterwords:
                    if str(word).find(filterword) == -1:
                        isAdd = False
                        break
                if isAdd:
                    keywords.add(word)
                    number += 1
            else:
                keywords.add(word)
                number += 1
        return keywords

