// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common.proto

package sjsu.cmpe.B295.common;

public final class CommonProto {
  private CommonProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface HeaderOrBuilder extends
      // @@protoc_insertion_point(interface_extends:sjsu.cmpe.B295.Header)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required int32 node_id = 1;</code>
     */
    boolean hasNodeId();
    /**
     * <code>required int32 node_id = 1;</code>
     */
    int getNodeId();

    /**
     * <code>required int64 time = 2;</code>
     */
    boolean hasTime();
    /**
     * <code>required int64 time = 2;</code>
     */
    long getTime();

    /**
     * <code>optional int32 destination = 8;</code>
     *
     * <pre>
     * if the message is for a specific node, this will be set
     * </pre>
     */
    boolean hasDestination();
    /**
     * <code>optional int32 destination = 8;</code>
     *
     * <pre>
     * if the message is for a specific node, this will be set
     * </pre>
     */
    int getDestination();
  }
  /**
   * Protobuf type {@code sjsu.cmpe.B295.Header}
   */
  public static final class Header extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:sjsu.cmpe.B295.Header)
      HeaderOrBuilder {
    // Use Header.newBuilder() to construct.
    private Header(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Header(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Header defaultInstance;
    public static Header getDefaultInstance() {
      return defaultInstance;
    }

    public Header getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Header(
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
              bitField0_ |= 0x00000001;
              nodeId_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              time_ = input.readInt64();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000004;
              destination_ = input.readInt32();
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
      return sjsu.cmpe.B295.common.CommonProto.internal_static_sjsu_cmpe_B295_Header_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return sjsu.cmpe.B295.common.CommonProto.internal_static_sjsu_cmpe_B295_Header_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              sjsu.cmpe.B295.common.CommonProto.Header.class, sjsu.cmpe.B295.common.CommonProto.Header.Builder.class);
    }

    public static com.google.protobuf.Parser<Header> PARSER =
        new com.google.protobuf.AbstractParser<Header>() {
      public Header parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Header(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Header> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int NODE_ID_FIELD_NUMBER = 1;
    private int nodeId_;
    /**
     * <code>required int32 node_id = 1;</code>
     */
    public boolean hasNodeId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required int32 node_id = 1;</code>
     */
    public int getNodeId() {
      return nodeId_;
    }

    public static final int TIME_FIELD_NUMBER = 2;
    private long time_;
    /**
     * <code>required int64 time = 2;</code>
     */
    public boolean hasTime() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required int64 time = 2;</code>
     */
    public long getTime() {
      return time_;
    }

    public static final int DESTINATION_FIELD_NUMBER = 8;
    private int destination_;
    /**
     * <code>optional int32 destination = 8;</code>
     *
     * <pre>
     * if the message is for a specific node, this will be set
     * </pre>
     */
    public boolean hasDestination() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int32 destination = 8;</code>
     *
     * <pre>
     * if the message is for a specific node, this will be set
     * </pre>
     */
    public int getDestination() {
      return destination_;
    }

    private void initFields() {
      nodeId_ = 0;
      time_ = 0L;
      destination_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasNodeId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasTime()) {
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
        output.writeInt32(1, nodeId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, time_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(8, destination_);
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
          .computeInt32Size(1, nodeId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, time_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, destination_);
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

    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static sjsu.cmpe.B295.common.CommonProto.Header parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(sjsu.cmpe.B295.common.CommonProto.Header prototype) {
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
     * Protobuf type {@code sjsu.cmpe.B295.Header}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:sjsu.cmpe.B295.Header)
        sjsu.cmpe.B295.common.CommonProto.HeaderOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return sjsu.cmpe.B295.common.CommonProto.internal_static_sjsu_cmpe_B295_Header_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return sjsu.cmpe.B295.common.CommonProto.internal_static_sjsu_cmpe_B295_Header_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                sjsu.cmpe.B295.common.CommonProto.Header.class, sjsu.cmpe.B295.common.CommonProto.Header.Builder.class);
      }

      // Construct using sjsu.cmpe.B295.common.CommonProto.Header.newBuilder()
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
        nodeId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        time_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        destination_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return sjsu.cmpe.B295.common.CommonProto.internal_static_sjsu_cmpe_B295_Header_descriptor;
      }

      public sjsu.cmpe.B295.common.CommonProto.Header getDefaultInstanceForType() {
        return sjsu.cmpe.B295.common.CommonProto.Header.getDefaultInstance();
      }

      public sjsu.cmpe.B295.common.CommonProto.Header build() {
        sjsu.cmpe.B295.common.CommonProto.Header result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public sjsu.cmpe.B295.common.CommonProto.Header buildPartial() {
        sjsu.cmpe.B295.common.CommonProto.Header result = new sjsu.cmpe.B295.common.CommonProto.Header(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.nodeId_ = nodeId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.time_ = time_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.destination_ = destination_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof sjsu.cmpe.B295.common.CommonProto.Header) {
          return mergeFrom((sjsu.cmpe.B295.common.CommonProto.Header)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(sjsu.cmpe.B295.common.CommonProto.Header other) {
        if (other == sjsu.cmpe.B295.common.CommonProto.Header.getDefaultInstance()) return this;
        if (other.hasNodeId()) {
          setNodeId(other.getNodeId());
        }
        if (other.hasTime()) {
          setTime(other.getTime());
        }
        if (other.hasDestination()) {
          setDestination(other.getDestination());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasNodeId()) {
          
          return false;
        }
        if (!hasTime()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        sjsu.cmpe.B295.common.CommonProto.Header parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (sjsu.cmpe.B295.common.CommonProto.Header) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int nodeId_ ;
      /**
       * <code>required int32 node_id = 1;</code>
       */
      public boolean hasNodeId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required int32 node_id = 1;</code>
       */
      public int getNodeId() {
        return nodeId_;
      }
      /**
       * <code>required int32 node_id = 1;</code>
       */
      public Builder setNodeId(int value) {
        bitField0_ |= 0x00000001;
        nodeId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 node_id = 1;</code>
       */
      public Builder clearNodeId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        nodeId_ = 0;
        onChanged();
        return this;
      }

      private long time_ ;
      /**
       * <code>required int64 time = 2;</code>
       */
      public boolean hasTime() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required int64 time = 2;</code>
       */
      public long getTime() {
        return time_;
      }
      /**
       * <code>required int64 time = 2;</code>
       */
      public Builder setTime(long value) {
        bitField0_ |= 0x00000002;
        time_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int64 time = 2;</code>
       */
      public Builder clearTime() {
        bitField0_ = (bitField0_ & ~0x00000002);
        time_ = 0L;
        onChanged();
        return this;
      }

      private int destination_ ;
      /**
       * <code>optional int32 destination = 8;</code>
       *
       * <pre>
       * if the message is for a specific node, this will be set
       * </pre>
       */
      public boolean hasDestination() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int32 destination = 8;</code>
       *
       * <pre>
       * if the message is for a specific node, this will be set
       * </pre>
       */
      public int getDestination() {
        return destination_;
      }
      /**
       * <code>optional int32 destination = 8;</code>
       *
       * <pre>
       * if the message is for a specific node, this will be set
       * </pre>
       */
      public Builder setDestination(int value) {
        bitField0_ |= 0x00000004;
        destination_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 destination = 8;</code>
       *
       * <pre>
       * if the message is for a specific node, this will be set
       * </pre>
       */
      public Builder clearDestination() {
        bitField0_ = (bitField0_ & ~0x00000004);
        destination_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:sjsu.cmpe.B295.Header)
    }

    static {
      defaultInstance = new Header(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:sjsu.cmpe.B295.Header)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_sjsu_cmpe_B295_Header_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_sjsu_cmpe_B295_Header_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014common.proto\022\016sjsu.cmpe.B295\"<\n\006Header" +
      "\022\017\n\007node_id\030\001 \002(\005\022\014\n\004time\030\002 \002(\003\022\023\n\013desti" +
      "nation\030\010 \001(\005B$\n\025sjsu.cmpe.B295.commonB\013C" +
      "ommonProto"
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
    internal_static_sjsu_cmpe_B295_Header_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_sjsu_cmpe_B295_Header_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_sjsu_cmpe_B295_Header_descriptor,
        new java.lang.String[] { "NodeId", "Time", "Destination", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
