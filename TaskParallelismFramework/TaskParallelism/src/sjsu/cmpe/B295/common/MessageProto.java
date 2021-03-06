// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package sjsu.cmpe.B295.common;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:sjsu.cmpe.B295.Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
     */
    boolean hasMessageType();
    /**
     * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
     */
    sjsu.cmpe.B295.common.MessageProto.Message.MessageType getMessageType();

    /**
     * <code>optional string messageStr = 2;</code>
     */
    boolean hasMessageStr();
    /**
     * <code>optional string messageStr = 2;</code>
     */
    java.lang.String getMessageStr();
    /**
     * <code>optional string messageStr = 2;</code>
     */
    com.google.protobuf.ByteString
        getMessageStrBytes();
  }
  /**
   * Protobuf type {@code sjsu.cmpe.B295.Message}
   */
  public static final class Message extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:sjsu.cmpe.B295.Message)
      MessageOrBuilder {
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Message(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Message defaultInstance;
    public static Message getDefaultInstance() {
      return defaultInstance;
    }

    public Message getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              sjsu.cmpe.B295.common.MessageProto.Message.MessageType value = sjsu.cmpe.B295.common.MessageProto.Message.MessageType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                messageType_ = value;
              }
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              messageStr_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return sjsu.cmpe.B295.common.MessageProto.internal_static_sjsu_cmpe_B295_Message_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return sjsu.cmpe.B295.common.MessageProto.internal_static_sjsu_cmpe_B295_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              sjsu.cmpe.B295.common.MessageProto.Message.class, sjsu.cmpe.B295.common.MessageProto.Message.Builder.class);
    }

    public static com.google.protobuf.Parser<Message> PARSER =
        new com.google.protobuf.AbstractParser<Message>() {
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Message(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    /**
     * Protobuf enum {@code sjsu.cmpe.B295.Message.MessageType}
     */
    public enum MessageType
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>INPUT = 0;</code>
       */
      INPUT(0, 0),
      /**
       * <code>INPUT_RESULT = 1;</code>
       */
      INPUT_RESULT(1, 1),
      /**
       * <code>ANALYZE = 2;</code>
       */
      ANALYZE(2, 2),
      /**
       * <code>ANALYSIS_RESULT = 3;</code>
       */
      ANALYSIS_RESULT(3, 3),
      /**
       * <code>SPLIT = 4;</code>
       */
      SPLIT(4, 4),
      /**
       * <code>SPLIT_RESULT = 5;</code>
       */
      SPLIT_RESULT(5, 5),
      /**
       * <code>MAP = 6;</code>
       */
      MAP(6, 6),
      /**
       * <code>MAP_RESULT = 7;</code>
       */
      MAP_RESULT(7, 7),
      /**
       * <code>REDUCE = 8;</code>
       */
      REDUCE(8, 8),
      /**
       * <code>REDUCE_RESULT = 9;</code>
       */
      REDUCE_RESULT(9, 9),
      ;

      /**
       * <code>INPUT = 0;</code>
       */
      public static final int INPUT_VALUE = 0;
      /**
       * <code>INPUT_RESULT = 1;</code>
       */
      public static final int INPUT_RESULT_VALUE = 1;
      /**
       * <code>ANALYZE = 2;</code>
       */
      public static final int ANALYZE_VALUE = 2;
      /**
       * <code>ANALYSIS_RESULT = 3;</code>
       */
      public static final int ANALYSIS_RESULT_VALUE = 3;
      /**
       * <code>SPLIT = 4;</code>
       */
      public static final int SPLIT_VALUE = 4;
      /**
       * <code>SPLIT_RESULT = 5;</code>
       */
      public static final int SPLIT_RESULT_VALUE = 5;
      /**
       * <code>MAP = 6;</code>
       */
      public static final int MAP_VALUE = 6;
      /**
       * <code>MAP_RESULT = 7;</code>
       */
      public static final int MAP_RESULT_VALUE = 7;
      /**
       * <code>REDUCE = 8;</code>
       */
      public static final int REDUCE_VALUE = 8;
      /**
       * <code>REDUCE_RESULT = 9;</code>
       */
      public static final int REDUCE_RESULT_VALUE = 9;


      public final int getNumber() { return value; }

      public static MessageType valueOf(int value) {
        switch (value) {
          case 0: return INPUT;
          case 1: return INPUT_RESULT;
          case 2: return ANALYZE;
          case 3: return ANALYSIS_RESULT;
          case 4: return SPLIT;
          case 5: return SPLIT_RESULT;
          case 6: return MAP;
          case 7: return MAP_RESULT;
          case 8: return REDUCE;
          case 9: return REDUCE_RESULT;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<MessageType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<MessageType>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<MessageType>() {
              public MessageType findValueByNumber(int number) {
                return MessageType.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return sjsu.cmpe.B295.common.MessageProto.Message.getDescriptor().getEnumTypes().get(0);
      }

      private static final MessageType[] VALUES = values();

      public static MessageType valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private MessageType(int index, int value) {
        this.index = index;
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:sjsu.cmpe.B295.Message.MessageType)
    }

    private int bitField0_;
    public static final int MESSAGETYPE_FIELD_NUMBER = 1;
    private sjsu.cmpe.B295.common.MessageProto.Message.MessageType messageType_;
    /**
     * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
     */
    public boolean hasMessageType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
     */
    public sjsu.cmpe.B295.common.MessageProto.Message.MessageType getMessageType() {
      return messageType_;
    }

    public static final int MESSAGESTR_FIELD_NUMBER = 2;
    private java.lang.Object messageStr_;
    /**
     * <code>optional string messageStr = 2;</code>
     */
    public boolean hasMessageStr() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string messageStr = 2;</code>
     */
    public java.lang.String getMessageStr() {
      java.lang.Object ref = messageStr_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          messageStr_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string messageStr = 2;</code>
     */
    public com.google.protobuf.ByteString
        getMessageStrBytes() {
      java.lang.Object ref = messageStr_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        messageStr_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      messageType_ = sjsu.cmpe.B295.common.MessageProto.Message.MessageType.INPUT;
      messageStr_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasMessageType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, messageType_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getMessageStrBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, messageType_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getMessageStrBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static sjsu.cmpe.B295.common.MessageProto.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(sjsu.cmpe.B295.common.MessageProto.Message prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code sjsu.cmpe.B295.Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:sjsu.cmpe.B295.Message)
        sjsu.cmpe.B295.common.MessageProto.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return sjsu.cmpe.B295.common.MessageProto.internal_static_sjsu_cmpe_B295_Message_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return sjsu.cmpe.B295.common.MessageProto.internal_static_sjsu_cmpe_B295_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                sjsu.cmpe.B295.common.MessageProto.Message.class, sjsu.cmpe.B295.common.MessageProto.Message.Builder.class);
      }

      // Construct using sjsu.cmpe.B295.common.MessageProto.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        messageType_ = sjsu.cmpe.B295.common.MessageProto.Message.MessageType.INPUT;
        bitField0_ = (bitField0_ & ~0x00000001);
        messageStr_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return sjsu.cmpe.B295.common.MessageProto.internal_static_sjsu_cmpe_B295_Message_descriptor;
      }

      public sjsu.cmpe.B295.common.MessageProto.Message getDefaultInstanceForType() {
        return sjsu.cmpe.B295.common.MessageProto.Message.getDefaultInstance();
      }

      public sjsu.cmpe.B295.common.MessageProto.Message build() {
        sjsu.cmpe.B295.common.MessageProto.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public sjsu.cmpe.B295.common.MessageProto.Message buildPartial() {
        sjsu.cmpe.B295.common.MessageProto.Message result = new sjsu.cmpe.B295.common.MessageProto.Message(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.messageType_ = messageType_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.messageStr_ = messageStr_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof sjsu.cmpe.B295.common.MessageProto.Message) {
          return mergeFrom((sjsu.cmpe.B295.common.MessageProto.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(sjsu.cmpe.B295.common.MessageProto.Message other) {
        if (other == sjsu.cmpe.B295.common.MessageProto.Message.getDefaultInstance()) return this;
        if (other.hasMessageType()) {
          setMessageType(other.getMessageType());
        }
        if (other.hasMessageStr()) {
          bitField0_ |= 0x00000002;
          messageStr_ = other.messageStr_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasMessageType()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        sjsu.cmpe.B295.common.MessageProto.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (sjsu.cmpe.B295.common.MessageProto.Message) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private sjsu.cmpe.B295.common.MessageProto.Message.MessageType messageType_ = sjsu.cmpe.B295.common.MessageProto.Message.MessageType.INPUT;
      /**
       * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
       */
      public boolean hasMessageType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
       */
      public sjsu.cmpe.B295.common.MessageProto.Message.MessageType getMessageType() {
        return messageType_;
      }
      /**
       * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
       */
      public Builder setMessageType(sjsu.cmpe.B295.common.MessageProto.Message.MessageType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        messageType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required .sjsu.cmpe.B295.Message.MessageType messageType = 1;</code>
       */
      public Builder clearMessageType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        messageType_ = sjsu.cmpe.B295.common.MessageProto.Message.MessageType.INPUT;
        onChanged();
        return this;
      }

      private java.lang.Object messageStr_ = "";
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public boolean hasMessageStr() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public java.lang.String getMessageStr() {
        java.lang.Object ref = messageStr_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            messageStr_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public com.google.protobuf.ByteString
          getMessageStrBytes() {
        java.lang.Object ref = messageStr_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          messageStr_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public Builder setMessageStr(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        messageStr_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public Builder clearMessageStr() {
        bitField0_ = (bitField0_ & ~0x00000002);
        messageStr_ = getDefaultInstance().getMessageStr();
        onChanged();
        return this;
      }
      /**
       * <code>optional string messageStr = 2;</code>
       */
      public Builder setMessageStrBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        messageStr_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:sjsu.cmpe.B295.Message)
    }

    static {
      defaultInstance = new Message(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:sjsu.cmpe.B295.Message)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_sjsu_cmpe_B295_Message_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_sjsu_cmpe_B295_Message_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rmessage.proto\022\016sjsu.cmpe.B295\"\373\001\n\007Mess" +
      "age\0228\n\013messageType\030\001 \002(\0162#.sjsu.cmpe.B29" +
      "5.Message.MessageType\022\022\n\nmessageStr\030\002 \001(" +
      "\t\"\241\001\n\013MessageType\022\t\n\005INPUT\020\000\022\020\n\014INPUT_RE" +
      "SULT\020\001\022\013\n\007ANALYZE\020\002\022\023\n\017ANALYSIS_RESULT\020\003" +
      "\022\t\n\005SPLIT\020\004\022\020\n\014SPLIT_RESULT\020\005\022\007\n\003MAP\020\006\022\016" +
      "\n\nMAP_RESULT\020\007\022\n\n\006REDUCE\020\010\022\021\n\rREDUCE_RES" +
      "ULT\020\tB%\n\025sjsu.cmpe.B295.commonB\014MessageP" +
      "roto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_sjsu_cmpe_B295_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_sjsu_cmpe_B295_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_sjsu_cmpe_B295_Message_descriptor,
        new java.lang.String[] { "MessageType", "MessageStr", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
