package pl.lotto.domain.numberreceiver;

class HashGeneratorTestImpl implements HashGenerable{

    private final String hash;

    HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    @Override
    public String getHash() {
        return this.hash;
    }
}
